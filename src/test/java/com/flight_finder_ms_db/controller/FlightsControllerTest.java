package com.flight_finder_ms_db.controller;

import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightsControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightsController flightsController;

    private FlightDTO sampleFlight;

    @BeforeEach
    void setUp() {
        sampleFlight = new FlightDTO();
        sampleFlight.setId(1L);
        sampleFlight.setPrice(new BigDecimal("199.99"));
        sampleFlight.setCurrency("EUR");
        sampleFlight.setSegments(Collections.emptyList());
        sampleFlight.setLayovers(Collections.emptyList());
    }

    @Test
    void searchFlightsSuccess() {
        when(flightService.searchFlights("MAD", "BCN")).thenReturn(List.of(sampleFlight));

        ResponseEntity<?> result = flightsController.searchFlights("MAD", "BCN");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertInstanceOf(List.class, result.getBody());
        @SuppressWarnings("unchecked")
        List<FlightDTO> body = (List<FlightDTO>) result.getBody();
        assertEquals(1, body.size());
        assertEquals(1L, body.get(0).getId());
        verify(flightService, times(1)).searchFlights("MAD", "BCN");
    }

    @Test
    void searchFlightsBadRequest() {
        when(flightService.searchFlights("", "BCN"))
                .thenThrow(new IllegalArgumentException("El código IATA del aeropuerto de origen es obligatorio."));

        ResponseEntity<?> result = flightsController.searchFlights("", "BCN");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("El código IATA del aeropuerto de origen es obligatorio.", result.getBody());
        verify(flightService, times(1)).searchFlights("", "BCN");
    }
}
