package com.app.proyecto.api.dto.request;

import com.app.proyecto.api.enums.EstadoTitulo;
import com.app.proyecto.api.enums.TipoTitulo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TituloRequest {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El tipo es requerido")
    private TipoTitulo tipo;

    private EstadoTitulo estado;
}