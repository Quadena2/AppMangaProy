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
public class CapituloResponse {
    private Long id;
    private Integer numero;
    private String nombre;
    private Long tituloId;
    private String tituloNombre;
    private int totalPaginas;
    private LocalDateTime fechaPublicacion;
}