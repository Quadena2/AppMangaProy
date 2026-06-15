package com.app.proyecto.api.dto.response;

import com.app.proyecto.api.enums.EstadoTitulo;
import com.app.proyecto.api.enums.TipoTitulo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TituloResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private TipoTitulo tipo;
    private String portadaUrl;
    private EstadoTitulo estado;
    private String autorNombre;
    private Long autorId;
    private int totalCapitulos;
    private LocalDateTime fechaCreacion;
}