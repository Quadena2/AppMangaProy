package com.app.proyecto.api.service;

import com.app.proyecto.api.dto.response.FavoritoResponse;
import com.app.proyecto.api.dto.response.PerfilResponse;
import com.app.proyecto.api.dto.response.TituloResponse;
import com.app.proyecto.api.entity.Usuario;
import com.app.proyecto.api.repository.FavoritoRepository;
import com.app.proyecto.api.repository.TituloRepository;
import com.app.proyecto.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final TituloRepository tituloRepository;
    private final FavoritoRepository favoritoRepository;
    private final FavoritoService favoritoService;

    public PerfilResponse obtenerPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<TituloResponse> misTitulos = tituloRepository
                .findByAutorId(usuario.getId())
                .stream()
                .map(t -> TituloResponse.builder()
                        .id(t.getId())
                        .nombre(t.getNombre())
                        .descripcion(t.getDescripcion())
                        .tipo(t.getTipo())
                        .portadaUrl(t.getPortadaUrl())
                        .estado(t.getEstado())
                        .autorNombre(t.getAutor().getNombre())
                        .autorId(t.getAutor().getId())
                        .totalCapitulos(t.getCapitulos() != null ? t.getCapitulos().size() : 0)
                        .fechaCreacion(t.getFechaCreacion())
                        .build()
                )
                .collect(Collectors.toList());

        List<FavoritoResponse> favoritos = favoritoService.listarFavoritos(email);

        return PerfilResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .fotoPerfil(usuario.getFotoPerfil())
                .rol(usuario.getRol().name())
                .provider(usuario.getProvider())
                .fechaRegistro(usuario.getFechaRegistro())
                .totalTitulos(misTitulos.size())
                .totalFavoritos(favoritos.size())
                .misTitulos(misTitulos)
                .favoritos(favoritos)
                .build();
    }
}
