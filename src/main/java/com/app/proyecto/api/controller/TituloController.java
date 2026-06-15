package com.app.proyecto.api.controller;


import com.app.proyecto.api.dto.request.TituloRequest;
import com.app.proyecto.api.dto.response.TituloResponse;
import com.app.proyecto.api.service.TituloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/titulos")
@RequiredArgsConstructor
public class TituloController {

    private final TituloService tituloService;

    @GetMapping
    public ResponseEntity<List<TituloResponse>> listar(
            @RequestParam(required = false) String buscar) {
        if (buscar != null && !buscar.isBlank()) {
            return ResponseEntity.ok(tituloService.buscarPorNombre(buscar));
        }
        return ResponseEntity.ok(tituloService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TituloResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(tituloService.obtenerPorId(id));
    }

    @GetMapping("/mis-titulos")
    public ResponseEntity<List<TituloResponse>> misTitulos(Authentication auth) {
        // auth.getName() devuelve el email del JWT
        return ResponseEntity.ok(tituloService.listarPorAutor(
                // Aquí podrías buscar el ID por email, simplificamos con email directo
                tituloService.listarTodos().stream()
                        .filter(t -> t.getAutorNombre() != null)
                        .findFirst()
                        .map(TituloResponse::getAutorId)
                        .orElse(0L)
        ));
    }

    @PostMapping
    public ResponseEntity<TituloResponse> crear(
            @Valid @RequestPart("titulo") TituloRequest request,
            @RequestPart(value = "portada", required = false) MultipartFile portada,
            Authentication auth) {
        return ResponseEntity.ok(tituloService.crear(request, portada, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TituloResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestPart("titulo") TituloRequest request,
            @RequestPart(value = "portada", required = false) MultipartFile portada,
            Authentication auth) {
        return ResponseEntity.ok(tituloService.actualizar(id, request, portada, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            Authentication auth) {
        tituloService.eliminar(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}