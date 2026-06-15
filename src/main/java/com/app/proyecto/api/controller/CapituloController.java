package com.app.proyecto.api.controller;


import com.app.proyecto.api.dto.request.CapituloRequest;
import com.app.proyecto.api.dto.response.CapituloResponse;
import com.app.proyecto.api.service.CapituloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CapituloController {

    private final CapituloService capituloService;

    @GetMapping("/titulos/{tituloId}/capitulos")
    public ResponseEntity<List<CapituloResponse>> listar(@PathVariable Long tituloId) {
        return ResponseEntity.ok(capituloService.listarPorTitulo(tituloId));
    }

    @GetMapping("/capitulos/{id}")
    public ResponseEntity<CapituloResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(capituloService.obtenerPorId(id));
    }

    @GetMapping("/capitulos/{id}/texto")
    public ResponseEntity<String> obtenerTexto(@PathVariable Long id) {
        return ResponseEntity.ok(capituloService.obtenerTextoNovela(id));
    }

    @PostMapping("/titulos/{tituloId}/capitulos")
    public ResponseEntity<CapituloResponse> crear(
            @PathVariable Long tituloId,
            @Valid @RequestBody CapituloRequest request,
            Authentication auth) {
        return ResponseEntity.ok(capituloService.crear(tituloId, request, auth.getName()));
    }
}