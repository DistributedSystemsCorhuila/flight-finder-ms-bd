package com.flight_finder_ms_db.service.impl;

import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.entity.Flight;
import com.flight_finder_ms_db.mapper.FlightMapper;
import com.flight_finder_ms_db.repository.FlightRepository;
import com.flight_finder_ms_db.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    private final FlightMapper flightMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FlightDTO> searchFlights(String departureAirportCode, String arrivalAirportCode) {
        String from = normalizeAirportCode(departureAirportCode, "origen");
        String to = normalizeAirportCode(arrivalAirportCode, "destino");

        if (from.equals(to)) {
            throw new IllegalArgumentException("El aeropuerto de origen y destino no pueden ser el mismo.");
        }

        List<Flight> flights = flightRepository.searchFlights(from, to);
        return flightMapper.toDTOs(flights);
    }

    private static String normalizeAirportCode(String code, String label) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("El código IATA del aeropuerto de " + label + " es obligatorio.");
        }
        String trimmed = code.trim().toUpperCase();
        if (trimmed.length() != 3) {
            throw new IllegalArgumentException("El código IATA de " + label + " debe tener 3 caracteres.");
        }
        return trimmed;
    }
}
