package com.app.proyecto.api.controller;


import com.app.proyecto.api.dto.response.PaginaResponse;
import com.app.proyecto.api.service.PaginaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/capitulos")
@RequiredArgsConstructor
public class PaginaController {

    private final PaginaService paginaService;

    @GetMapping("/{capituloId}/paginas")
    public ResponseEntity<List<PaginaResponse>> listar(@PathVariable Long capituloId) {
        return ResponseEntity.ok(paginaService.listarPorCapitulo(capituloId));
    }

    @PostMapping("/{capituloId}/paginas")
    public ResponseEntity<PaginaResponse> subirPagina(
            @PathVariable Long capituloId,
            @RequestParam("numero") Integer numero,
            @RequestParam("imagen") MultipartFile imagen,
            Authentication auth) {
        return ResponseEntity.ok(
                paginaService.subirPagina(capituloId, numero, imagen, auth.getName())
        );
    }
}