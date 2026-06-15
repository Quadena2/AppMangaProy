package com.app.proyecto.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponse {
    private Long id;
    private String nombre;
    private String email;
    private String fotoPerfil;
    private String rol;
    private String provider;
    private LocalDateTime fechaRegistro;
    private int totalTitulos;
    private int totalFavoritos;
    private List<TituloResponse> misTitulos;
    private List<FavoritoResponse> favoritos;
}
