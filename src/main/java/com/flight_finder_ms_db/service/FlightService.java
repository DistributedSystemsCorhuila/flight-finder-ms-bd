package com.flight_finder_ms_db.service;

import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.dto.FlightSearchRequest;

import java.util.List;

public interface FlightService {

    List<FlightDTO> searchFlights(Long userId, FlightSearchRequest request);
}
