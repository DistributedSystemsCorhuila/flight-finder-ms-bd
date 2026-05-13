package com.flight_finder_ms_db.repository;

import com.flight_finder_ms_db.entity.FlightSearchHistory;
import com.flight_finder_ms_db.dto.RouteCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightSearchHistoryRepository extends JpaRepository<FlightSearchHistory, Long> {

    @Query("""
        SELECT new com.flight_finder_ms_db.dto.RouteCountDTO(
            CONCAT(h.origin, '-', h.destination),
            COUNT(h)
        )
        FROM FlightSearchHistory h
        WHERE h.userId = :userId
        GROUP BY h.origin, h.destination
        ORDER BY COUNT(h) DESC
    """)
    List<RouteCountDTO> findTopRoutesByUserId(Long userId);
}