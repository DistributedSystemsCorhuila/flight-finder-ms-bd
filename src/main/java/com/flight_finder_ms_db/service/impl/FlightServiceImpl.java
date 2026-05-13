package com.flight_finder_ms_db.service.impl;

import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.dto.FlightSearchRequest;
import com.flight_finder_ms_db.entity.FlightSearchHistory;
import com.flight_finder_ms_db.service.FlightSearchHistoryService;
import com.flight_finder_ms_db.service.FlightService;
import com.flight_finder_ms_db.service.GoogleFlightsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final GoogleFlightsClient googleFlightsClient;
    private final FlightSearchHistoryService historyService;

    @Override
    public List<FlightDTO> searchFlights(Long userId, FlightSearchRequest request) {
        // Validar y normalizar inputs
        String normalizedOrigin = normalizeAndValidateAirportCode(request.getOrigin(), "origen");
        String normalizedDestination = normalizeAndValidateAirportCode(request.getDestination(), "destino");

        // Asignar valores normalizados
        request.setOrigin(normalizedOrigin);
        request.setDestination(normalizedDestination);

        if (normalizedOrigin.equals(normalizedDestination)) {
            throw new IllegalArgumentException("El aeropuerto de origen y destino no pueden ser el mismo.");
        }

        // Validar fechas
        validateDates(request);

        // Guardar en historial
        FlightSearchHistory history = FlightSearchHistory.builder()
                .userId(userId)
                .origin(normalizedOrigin)
                .destination(normalizedDestination)
                .departureDate(request.getDepartureDate())
                .returnDate(request.getReturnDate())
                .adults(request.getAdults())
                .cabinClass(request.getCabinClass())
                .searchedAt(LocalDateTime.now())
                .build();

        try {
            historyService.saveSearchHistory(history);
            log.info("Búsqueda guardada en historial: userId={}, origin={}, destination={}",
                    userId, normalizedOrigin, normalizedDestination);
        } catch (Exception e) {
            log.error("Error al guardar historial de búsqueda: {}", e.getMessage(), e);
            // No lanzar excepción, continuar con la búsqueda
        }

        // Llamar a API externa
        log.info("Consultando API de vuelos: {} -> {}", normalizedOrigin, normalizedDestination);
        return googleFlightsClient.searchFlights(request);
    }

    private String normalizeAndValidateAirportCode(String code, String label) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("El código IATA del aeropuerto de " + label + " es obligatorio.");
        }
        String trimmed = code.trim().toUpperCase();
        if (trimmed.length() != 3) {
            throw new IllegalArgumentException("El código IATA de " + label + " debe tener exactamente 3 caracteres. Recibido: " + trimmed);
        }
        return trimmed;
    }

    private void validateDates(FlightSearchRequest request) {
        if (request.getDepartureDate() == null) {
            throw new IllegalArgumentException("La fecha de salida es obligatoria.");
        }
        if (request.getAdults() == null || request.getAdults() < 1) {
            throw new IllegalArgumentException("Debe haber al menos 1 adulto.");
        }
    }
}