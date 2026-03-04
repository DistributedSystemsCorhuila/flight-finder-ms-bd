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
@Schema(description = "DTO de respuesta para login exitoso")
public class LoginResponse {

    @Schema(description = "Mensaje de estado", example = "Login exitoso")
    private String message;

    @Schema(description = "Información del usuario autenticado")
    private UserDTO user;

    // Este campo se utilizará cuando se implemente JWT
    @Schema(description = "Token de autenticación (se implementará con JWT)", example = "null")
    private String token;

    public LoginResponse(String message, UserDTO user) {
        this.message = message;
        this.user = user;
        this.token = null; // Se completará cuando se implemente JWT
    }
}

