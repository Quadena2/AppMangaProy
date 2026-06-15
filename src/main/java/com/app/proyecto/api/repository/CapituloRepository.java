package com.app.proyecto.api.repository;

import com.app.proyecto.api.entity.Capitulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CapituloRepository extends JpaRepository<Capitulo, Long> {

    List<Capitulo> findByTituloIdOrderByNumeroAsc(Long tituloId);
    Optional<Capitulo> findByTituloIdAndNumero(Long tituloId, Integer numero);
}
