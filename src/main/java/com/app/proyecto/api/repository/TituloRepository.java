package com.app.proyecto.api.repository;

import com.app.proyecto.api.entity.Titulo;
import com.app.proyecto.api.enums.TipoTitulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TituloRepository extends JpaRepository<Titulo, Long> {

    List<Titulo> findByTipo(TipoTitulo tipo);
    List<Titulo> findByAutorId(Long autorId);
    List<Titulo> findByNombreContainingIgnoreCase(String nombre);
}
