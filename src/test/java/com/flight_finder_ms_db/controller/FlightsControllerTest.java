package com.flight_finder_ms_db.controller;

import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.dto.FlightSearchRequest;
import com.flight_finder_ms_db.dto.RouteCountDTO;
import com.flight_finder_ms_db.service.FlightSearchHistoryService;
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
import java.time.LocalDate;
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

    @Mock
    private FlightSearchHistoryService historyService;

    @InjectMocks
    private FlightsController flightsController;

    private FlightDTO sampleFlight;
    private FlightSearchRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleFlight = new FlightDTO();
        sampleFlight.setId(1L);
        sampleFlight.setPrice(new BigDecimal("199.99"));
        sampleFlight.setCurrency("EUR");
        sampleFlight.setSegments(Collections.emptyList());
        sampleFlight.setLayovers(Collections.emptyList());

        sampleRequest = new FlightSearchRequest("MAD", "BCN", LocalDate.now().plusDays(1), null, 1, "ECONOMY");
    }

    @Test
    void searchFlightsSuccess() {
        Long userId = 1L;
        when(flightService.searchFlights(userId, sampleRequest)).thenReturn(List.of(sampleFlight));

        ResponseEntity<?> result = flightsController.searchFlights(
                userId,
                sampleRequest.getOrigin(),
                sampleRequest.getDestination(),
                sampleRequest.getDepartureDate(),
                sampleRequest.getReturnDate(),
                sampleRequest.getAdults(),
                sampleRequest.getCabinClass()
        );

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertInstanceOf(List.class, result.getBody());
        @SuppressWarnings("unchecked")
        List<FlightDTO> body = (List<FlightDTO>) result.getBody();
        assertEquals(1, body.size());
        assertEquals(1L, body.get(0).getId());
        verify(flightService, times(1)).searchFlights(userId, sampleRequest);
    }

    @Test
    void searchFlightsBadRequest() {
        Long userId = 1L;
        FlightSearchRequest invalidRequest = new FlightSearchRequest("", "BCN", LocalDate.now().plusDays(1), null, 1, "ECONOMY");

        when(flightService.searchFlights(userId, invalidRequest))
                .thenThrow(new IllegalArgumentException("El código IATA del aeropuerto de origen es obligatorio."));

        ResponseEntity<?> result = flightsController.searchFlights(
                userId,
                invalidRequest.getOrigin(),
                invalidRequest.getDestination(),
                invalidRequest.getDepartureDate(),
                invalidRequest.getReturnDate(),
                invalidRequest.getAdults(),
                invalidRequest.getCabinClass()
        );

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("El código IATA del aeropuerto de origen es obligatorio.", result.getBody());
        verify(flightService, times(1)).searchFlights(userId, invalidRequest);
    }

    @Test
    void getTopRoutesSuccess() {
        Long userId = 1L;
        List<RouteCountDTO> expectedRoutes = List.of(
                new RouteCountDTO("MAD-BCN", 5L),
                new RouteCountDTO("MAD-JFK", 3L)
        );

        when(historyService.getTopRoutesByUserId(userId)).thenReturn(expectedRoutes);

        ResponseEntity<List<RouteCountDTO>> result = flightsController.getTopRoutes(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        assertEquals("MAD-BCN", result.getBody().get(0).getRoute());
        assertEquals(5L, result.getBody().get(0).getCount());
        verify(historyService, times(1)).getTopRoutesByUserId(userId);
    }
}