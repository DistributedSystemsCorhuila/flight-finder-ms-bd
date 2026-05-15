package com.flight_finder_ms_db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight_segments")
public class FlightSegment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer segmentOrder;

    private String flightNumber;

    private String airline;

    private String airplane;

    private String travelClass;

    private String departureAirportCode;
    private String departureAirportName;

    private String arrivalAirportCode;
    private String arrivalAirportName;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private Integer duration;

    private Boolean delayed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;
}