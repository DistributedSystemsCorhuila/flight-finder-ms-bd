package com.flight_finder_ms_db.service;

import com.flight_finder_ms_db.dto.RouteCountDTO;
import com.flight_finder_ms_db.entity.FlightSearchHistory;

import java.util.List;

public interface FlightSearchHistoryService {

    void saveSearchHistory(FlightSearchHistory history);
    List<RouteCountDTO> getTopRoutesByUserId(Long userId);
}