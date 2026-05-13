package com.flight_finder_ms_db.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequest {

    private String origin;
    private String destination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Integer adults;
    private String cabinClass;
}