package com.flight_finder_ms_db.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.dto.FlightSearchRequest;
import com.flight_finder_ms_db.dto.FlightSegmentDTO;
import com.flight_finder_ms_db.dto.LayoverDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleFlightsClient {

    private final WebClient serpApiWebClient;

    @Value("${serpapi.api.key}")
    private String serpApiKey;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public List<FlightDTO> searchFlights(FlightSearchRequest request) {
        try {
            log.info("Buscando vuelos: {} -> {}", request.getOrigin(), request.getDestination());

            JsonNode root = serpApiWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search.json")
                            .queryParam("engine", "google_flights")
                            .queryParam("departure_id", request.getOrigin())
                            .queryParam("arrival_id", request.getDestination())
                            .queryParam("currency", "USD")
                            .queryParam("type", "2")
                            .queryParam("outbound_date", request.getDepartureDate().toString())
                            .queryParam("api_key", serpApiKey)
                            .build())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("Error en API externa: " + clientResponse.statusCode() + " - " + body))
                    )
                    .bodyToMono(JsonNode.class)
                    .timeout(Duration.ofSeconds(30)) // Timeout de 30 segundos
                    .block();

            if (root == null || !root.has("best_flights")) {
                log.warn("No se encontraron vuelos en la respuesta de la API");
                return Collections.emptyList();
            }

            List<FlightDTO> result = new ArrayList<>();
            for (JsonNode flightNode : root.get("best_flights")) {
                FlightDTO dto = mapFlightNodeToDTO(flightNode);
                result.add(dto);
            }

            log.info("Se encontraron {} vuelos", result.size());
            return result;

        } catch (WebClientResponseException e) {
            log.error("Error en la llamada a SerpAPI: {}", e.getMessage(), e);
            throw new RuntimeException("Error consultando API de vuelos: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error inesperado al buscar vuelos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar vuelos: " + e.getMessage(), e);
        }
    }

    private FlightDTO mapFlightNodeToDTO(JsonNode node) {
        FlightDTO dto = new FlightDTO();
        dto.setId(null);
        dto.setTotalDuration(node.path("total_duration").asInt());
        dto.setTotalStops(Math.max(0, node.path("flights").size() - 1)); // Evitar negativos
        dto.setPrice(new BigDecimal(node.path("price").asDouble(0)));
        dto.setCurrency("USD");
        dto.setTripType(node.path("type").asText());

        // Convertir de gramos a kg
        int carbonInGrams = node.path("carbon_emissions").path("this_flight").asInt(0);
        dto.setCarbonEmission(carbonInGrams / 1000);

        dto.setBookingToken(node.path("booking_token").asText());

        // Mapear segmentos
        List<FlightSegmentDTO> segments = new ArrayList<>();
        int idx = 0;
        for (JsonNode seg : node.path("flights")) {
            segments.add(mapSegmentNodeToDTO(seg, idx++));
        }
        dto.setSegments(segments);

        // Mapear escalas
        List<LayoverDTO> layovers = new ArrayList<>();
        for (JsonNode lay : node.path("layovers")) {
            layovers.add(mapLayoverNodeToDTO(lay));
        }
        dto.setLayovers(layovers);

        return dto;
    }

    private FlightSegmentDTO mapSegmentNodeToDTO(JsonNode seg, int order) {
        FlightSegmentDTO dto = new FlightSegmentDTO();
        dto.setId(null);
        dto.setSegmentOrder(order);
        dto.setFlightNumber(seg.path("flight_number").asText());
        dto.setAirline(seg.path("airline").asText());
        dto.setAirplane(seg.path("airplane").asText());
        dto.setTravelClass(seg.path("travel_class").asText());
        dto.setDepartureAirportCode(seg.path("departure_airport").path("id").asText());
        dto.setDepartureAirportName(seg.path("departure_airport").path("name").asText());
        dto.setArrivalAirportCode(seg.path("arrival_airport").path("id").asText());
        dto.setArrivalAirportName(seg.path("arrival_airport").path("name").asText());
        dto.setDepartureTime(parseDateTime(seg.path("departure_airport").path("time").asText()));
        dto.setArrivalTime(parseDateTime(seg.path("arrival_airport").path("time").asText()));
        dto.setDuration(seg.path("duration").asInt());
        dto.setDelayed(seg.has("often_delayed_by_over_30_min") ? seg.path("often_delayed_by_over_30_min").asBoolean() : false);
        return dto;
    }

    private LayoverDTO mapLayoverNodeToDTO(JsonNode lay) {
        LayoverDTO dto = new LayoverDTO();
        dto.setId(null);
        dto.setDuration(lay.path("duration").asInt());
        dto.setAirportCode(lay.path("id").asText());
        dto.setAirportName(lay.path("name").asText());
        return dto;
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, DATETIME_FORMATTER);
        } catch (Exception e) {
            log.warn("Error al parsear fecha: {}", value, e);
            return null;
        }
    }
}