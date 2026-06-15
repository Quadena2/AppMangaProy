package com.app.proyecto.api.repository;

import com.app.proyecto.api.entity.ContenidoNovela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContenidoNovelaRepository extends JpaRepository<ContenidoNovela, Long> {
    Optional<ContenidoNovela> findByCapituloId(Long capituloId);
}
