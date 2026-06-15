package com.app.proyecto.api.service;

import com.app.proyecto.api.dto.response.FavoritoResponse;
import com.app.proyecto.api.entity.Favorito;
import com.app.proyecto.api.entity.Titulo;
import com.app.proyecto.api.entity.Usuario;
import com.app.proyecto.api.repository.FavoritoRepository;
import com.app.proyecto.api.repository.TituloRepository;
import com.app.proyecto.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TituloRepository tituloRepository;

    public List<FavoritoResponse> listarFavoritos(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return favoritoRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public FavoritoResponse agregarFavorito(String email, Long tituloId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Titulo titulo = tituloRepository.findById(tituloId)
                .orElseThrow(() -> new RuntimeException("Título no encontrado"));

        if (favoritoRepository.existsByUsuarioIdAndTituloId(usuario.getId(), tituloId)) {
            throw new RuntimeException("Ya está en favoritos");
        }

        Favorito favorito = Favorito.builder()
                .usuario(usuario)
                .titulo(titulo)
                .build();

        return toResponse(favoritoRepository.save(favorito));
    }

    @Transactional
    public void eliminarFavorito(String email, Long tituloId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        favoritoRepository.deleteByUsuarioIdAndTituloId(usuario.getId(), tituloId);
    }

    public boolean esFavorito(String email, Long tituloId) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return favoritoRepository.existsByUsuarioIdAndTituloId(usuario.getId(), tituloId);
    }

    private FavoritoResponse toResponse(Favorito favorito) {
        return FavoritoResponse.builder()
                .id(favorito.getId())
                .tituloId(favorito.getTitulo().getId())
                .tituloNombre(favorito.getTitulo().getNombre())
                .portadaUrl(favorito.getTitulo().getPortadaUrl())
                .tipo(favorito.getTitulo().getTipo().name())
                .fechaAgregado(favorito.getFechaAgregado())
                .build();
    }
}
