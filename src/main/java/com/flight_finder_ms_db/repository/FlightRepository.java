package com.flight_finder_ms_db.repository;

import com.flight_finder_ms_db.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
    SELECT DISTINCT f
    FROM Flight f
    JOIN f.segments firstSegment
    JOIN f.segments lastSegment
    WHERE firstSegment.segmentOrder = 1
      AND firstSegment.departureAirportCode = :departureCode
      AND lastSegment.arrivalAirportCode = :arrivalCode
    ORDER BY f.price ASC
""")
    List<Flight> searchFlights(
            String departureCode,
            String arrivalCode
    );
}
