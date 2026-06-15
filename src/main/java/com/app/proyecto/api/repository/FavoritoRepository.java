package com.app.proyecto.api.repository;

import com.app.proyecto.api.entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuarioId(Long usuarioId);
    Optional<Favorito> findByUsuarioIdAndTituloId(Long usuarioId, Long tituloId);
    boolean existsByUsuarioIdAndTituloId(Long usuarioId, Long tituloId);
    void deleteByUsuarioIdAndTituloId(Long usuarioId, Long tituloId);
}