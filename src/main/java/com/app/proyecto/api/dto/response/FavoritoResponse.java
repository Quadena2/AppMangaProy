package com.app.proyecto.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoritoResponse {
    private Long id;
    private Long tituloId;
    private String tituloNombre;
    private String portadaUrl;
    private String tipo;
    private LocalDateTime fechaAgregado;
}