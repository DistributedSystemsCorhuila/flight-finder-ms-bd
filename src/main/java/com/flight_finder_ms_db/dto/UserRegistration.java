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
@Schema(description = "DTO para el registro de un nuevo usuario")
public class UserRegistration {

    @Schema(description = "Nombre del usuario", example = "John")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Doe")
    private String lastName;

    @Schema(description = "Nombre de usuario único", example = "john_doe")
    private String username;

    @Schema(description = "Correo electrónico único", example = "john@example.com")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}

