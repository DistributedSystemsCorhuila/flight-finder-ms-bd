package com.flight_finder_ms_db.mapper;

import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.dto.FlightSegmentDTO;
import com.flight_finder_ms_db.dto.LayoverDTO;
import com.flight_finder_ms_db.entity.Flight;
import com.flight_finder_ms_db.entity.FlightSegment;
import com.flight_finder_ms_db.entity.Layover;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FlightMapper {

    public FlightDTO toDTO(Flight flight) {
        if (flight == null) {
            return null;
        }
        return new FlightDTO(
                flight.getId(),
                flight.getTotalDuration(),
                flight.getTotalStops(),
                flight.getPrice(),
                flight.getCurrency(),
                flight.getTripType(),
                flight.getCarbonEmission(),
                flight.getBookingToken(),
                toSegmentDTOs(flight.getSegments()),
                toLayoverDTOs(flight.getLayovers())
        );
    }

    public List<FlightDTO> toDTOs(List<Flight> flights) {
        if (flights == null) {
            return Collections.emptyList();
        }
        return flights.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private List<FlightSegmentDTO> toSegmentDTOs(Set<FlightSegment> segments) {
        if (segments == null || segments.isEmpty()) {
            return Collections.emptyList();
        }
        return segments.stream()
                .sorted(Comparator.comparing(FlightSegment::getSegmentOrder, Comparator.nullsLast(Integer::compareTo)))
                .map(this::toSegmentDTO)
                .collect(Collectors.toList());
    }

    private FlightSegmentDTO toSegmentDTO(FlightSegment s) {
        if (s == null) {
            return null;
        }
        return new FlightSegmentDTO(
                s.getId(),
                s.getSegmentOrder(),
                s.getFlightNumber(),
                s.getAirline(),
                s.getAirplane(),
                s.getTravelClass(),
                s.getDepartureAirportCode(),
                s.getDepartureAirportName(),
                s.getArrivalAirportCode(),
                s.getArrivalAirportName(),
                s.getDepartureTime(),
                s.getArrivalTime(),
                s.getDuration(),
                s.getDelayed()
        );
    }

    private List<LayoverDTO> toLayoverDTOs(Set<Layover> layovers) {
        if (layovers == null || layovers.isEmpty()) {
            return Collections.emptyList();
        }
        return layovers.stream().map(this::toLayoverDTO).collect(Collectors.toList());
    }

    private LayoverDTO toLayoverDTO(Layover l) {
        if (l == null) {
            return null;
        }
        return new LayoverDTO(
                l.getId(),
                l.getDuration(),
                l.getAirportCode(),
                l.getAirportName()
        );
    }
}
