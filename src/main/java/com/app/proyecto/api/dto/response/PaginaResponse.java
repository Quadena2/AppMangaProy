package com.app.proyecto.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginaResponse {
    private Long id;
    private Integer numero;
    private String imagenUrl;
    private Long capituloId;
}
