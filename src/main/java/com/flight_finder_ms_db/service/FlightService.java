package com.flight_finder_ms_db.service;

import com.flight_finder_ms_db.dto.FlightDTO;

import java.util.List;

public interface FlightService {

    List<FlightDTO> searchFlights(String departureAirportCode, String arrivalAirportCode);
}
