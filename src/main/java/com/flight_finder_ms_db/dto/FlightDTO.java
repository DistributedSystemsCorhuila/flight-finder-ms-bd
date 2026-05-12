package com.flight_finder_ms_db.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    private Long id;
    private Integer totalDuration;
    private Integer totalStops;
    private BigDecimal price;
    private String currency;
    private String tripType;
    private Integer carbonEmission;
    private String bookingToken;
    private List<FlightSegmentDTO> segments;
    private List<LayoverDTO> layovers;
}
