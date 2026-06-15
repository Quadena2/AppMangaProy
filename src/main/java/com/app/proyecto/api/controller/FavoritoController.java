package com.app.proyecto.api.controller;

import com.app.proyecto.api.dto.response.FavoritoResponse;
import com.app.proyecto.api.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @GetMapping
    public ResponseEntity<List<FavoritoResponse>> listar(Authentication auth) {
        return ResponseEntity.ok(favoritoService.listarFavoritos(auth.getName()));
    }

    @PostMapping("/{tituloId}")
    public ResponseEntity<FavoritoResponse> agregar(
            @PathVariable Long tituloId,
            Authentication auth) {
        return ResponseEntity.ok(favoritoService.agregarFavorito(auth.getName(), tituloId));
    }

    @DeleteMapping("/{tituloId}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long tituloId,
            Authentication auth) {
        favoritoService.eliminarFavorito(auth.getName(), tituloId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tituloId}/check")
    public ResponseEntity<Boolean> check(
            @PathVariable Long tituloId,
            Authentication auth) {
        return ResponseEntity.ok(favoritoService.esFavorito(auth.getName(), tituloId));
    }
}
