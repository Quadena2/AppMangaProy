package com.app.proyecto.api.repository;

import com.app.proyecto.api.entity.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaginaRepository extends JpaRepository<Pagina, Long> {

    List<Pagina> findByCapituloIdOrderByNumeroAsc(Long capituloId);
}
