package com.app.proyecto.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CapituloRequest {

    @NotNull(message = "El número de capítulo es requerido")
    private Integer numero;

    private String nombre;

    // Solo para novelas
    private String texto;
}