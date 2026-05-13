package com.flight_finder_ms_db.controller;

import com.flight_finder_ms_db.dto.FlightDTO;
import com.flight_finder_ms_db.dto.FlightSearchRequest;
import com.flight_finder_ms_db.dto.RouteCountDTO;
import com.flight_finder_ms_db.service.FlightSearchHistoryService;
import com.flight_finder_ms_db.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
@Tag(name = "Flights", description = "Flight search API")
public class FlightsController {

    private final FlightService flightService;
    private final FlightSearchHistoryService historyService;

    @Operation(summary = "Buscar vuelos", description = "Busca vuelos via API externa y guarda historial.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vuelos",
                    content = @Content(schema = @Schema(implementation = FlightDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(
            @RequestParam("userId") Long userId,
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination,
            @RequestParam("departureDate") LocalDate departureDate,
            @RequestParam(value = "returnDate", required = false) LocalDate returnDate,
            @RequestParam("adults") Integer adults,
            @RequestParam(value = "cabinClass", required = false) String cabinClass) {

        FlightSearchRequest request = new FlightSearchRequest(origin, destination, departureDate, returnDate, adults, cabinClass);
        try {
            List<FlightDTO> flights = flightService.searchFlights(userId, request);
            return ResponseEntity.ok(flights);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener rutas más buscadas por usuario")
    @GetMapping("/history/top/{userId}")
    public ResponseEntity<List<RouteCountDTO>> getTopRoutes(@PathVariable Long userId) {
        List<RouteCountDTO> routes = historyService.getTopRoutesByUserId(userId);
        return ResponseEntity.ok(routes);
    }
}