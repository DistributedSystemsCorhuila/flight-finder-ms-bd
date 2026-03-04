package com.flight_finder_ms_db.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con información del usuario")
public class UserDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "John")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Doe")
    private String lastName;

    @Schema(description = "Nombre de usuario", example = "john_doe")
    private String username;

    @Schema(description = "Destinos favoritos", example = "Italia")
    private String favoriteDestinations;
}

