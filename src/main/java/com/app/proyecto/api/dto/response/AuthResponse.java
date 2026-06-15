package com.app.proyecto.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String jwt;
    private String rol;
    private String nombre;
    private String email;
    private String fotoPerfil;
}
