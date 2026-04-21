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
@Schema(description = "DTO para la solicitud de login")
public class LoginRequest {

    @Schema(description = "Nombre de usuario", example = "john_doe", required = true)
    private String username;

    @Schema(description = "Contraseña del usuario", example = "password123", required = true)
    private String password;
}

