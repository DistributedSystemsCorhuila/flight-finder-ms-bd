package com.flight_finder_ms_db.controller;

import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
@Tag(name = "Flights", description = "Flight search API")
public class FlightsController {

    private final FlightService flightService;

    @Operation(
            summary = "Buscar vuelos",
            description = "Lista vuelos cuyo primer tramo sale del aeropuerto de origen y algún tramo llega al destino, ordenados por precio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vuelos (puede estar vacía)",
                    content = @Content(schema = @Schema(implementation = FlightDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(
            @RequestParam("from") String departureAirportCode,
            @RequestParam("to") String arrivalAirportCode) {
        try {
            List<FlightDTO> flights = flightService.searchFlights(departureAirportCode, arrivalAirportCode);
            return ResponseEntity.ok(flights);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
