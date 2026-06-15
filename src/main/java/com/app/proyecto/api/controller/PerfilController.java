package com.app.proyecto.api.controller;

import com.app.proyecto.api.dto.response.PerfilResponse;
import com.app.proyecto.api.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public ResponseEntity<PerfilResponse> obtenerPerfil(Authentication auth) {
        return ResponseEntity.ok(perfilService.obtenerPerfil(auth.getName()));
    }
}
