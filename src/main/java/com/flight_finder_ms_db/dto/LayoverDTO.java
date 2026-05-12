package com.flight_finder_ms_db.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LayoverDTO {

    private Long id;
    private Integer duration;
    private String airportCode;
    private String airportName;
}
