package com.app.proyecto.api.repository;

import com.app.proyecto.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByGoogleId(String googleId);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String enail);
}
