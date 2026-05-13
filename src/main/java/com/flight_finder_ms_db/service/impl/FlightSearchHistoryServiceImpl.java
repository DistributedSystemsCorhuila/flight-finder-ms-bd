package com.flight_finder_ms_db.service.impl;

import com.flight_finder_ms_db.dto.RouteCountDTO;
import com.flight_finder_ms_db.entity.FlightSearchHistory;
import com.flight_finder_ms_db.repository.FlightSearchHistoryRepository;
import com.flight_finder_ms_db.service.FlightSearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightSearchHistoryServiceImpl implements FlightSearchHistoryService {

    private final FlightSearchHistoryRepository repository;

    @Override
    public void saveSearchHistory(FlightSearchHistory history) {
        repository.save(history);
    }

    @Override
    public List<RouteCountDTO> getTopRoutesByUserId(Long userId) {
        return repository.findTopRoutesByUserId(userId);
    }
}