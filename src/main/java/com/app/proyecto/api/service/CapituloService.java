package com.app.proyecto.api.service;


import com.app.proyecto.api.dto.request.CapituloRequest;
import com.app.proyecto.api.dto.response.CapituloResponse;
import com.app.proyecto.api.entity.Capitulo;
import com.app.proyecto.api.entity.ContenidoNovela;
import com.app.proyecto.api.entity.Titulo;
import com.app.proyecto.api.enums.TipoTitulo;
import com.app.proyecto.api.repository.CapituloRepository;
import com.app.proyecto.api.repository.ContenidoNovelaRepository;
import com.app.proyecto.api.repository.TituloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CapituloService {

    private final CapituloRepository capituloRepository;
    private final TituloRepository tituloRepository;
    private final ContenidoNovelaRepository contenidoNovelaRepository;

    public List<CapituloResponse> listarPorTitulo(Long tituloId) {
        return capituloRepository.findByTituloIdOrderByNumeroAsc(tituloId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CapituloResponse obtenerPorId(Long id) {
        Capitulo capitulo = capituloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capítulo no encontrado"));
        return toResponse(capitulo);
    }

    public String obtenerTextoNovela(Long capituloId) {
        ContenidoNovela contenido = contenidoNovelaRepository.findByCapituloId(capituloId)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));
        return contenido.getTexto();
    }

    public CapituloResponse crear(Long tituloId,
                                  CapituloRequest request,
                                  String emailAutor) {
        Titulo titulo = tituloRepository.findById(tituloId)
                .orElseThrow(() -> new RuntimeException("Título no encontrado"));

        if (!titulo.getAutor().getEmail().equals(emailAutor)) {
            throw new RuntimeException("No tienes permiso para agregar capítulos a este título");
        }

        Capitulo capitulo = Capitulo.builder()
                .titulo(titulo)
                .numero(request.getNumero())
                .nombre(request.getNombre())
                .build();

        Capitulo guardado = capituloRepository.save(capitulo);

        // Si es novela, guarda el texto
        if (titulo.getTipo() == TipoTitulo.NOVELA && request.getTexto() != null) {
            ContenidoNovela contenido = ContenidoNovela.builder()
                    .capitulo(guardado)
                    .texto(request.getTexto())
                    .build();
            contenidoNovelaRepository.save(contenido);
        }

        return toResponse(guardado);
    }

    private CapituloResponse toResponse(Capitulo capitulo) {
        return CapituloResponse.builder()
                .id(capitulo.getId())
                .numero(capitulo.getNumero())
                .nombre(capitulo.getNombre())
                .tituloId(capitulo.getTitulo().getId())
                .tituloNombre(capitulo.getTitulo().getNombre())
                .totalPaginas(capitulo.getPaginas() != null ? capitulo.getPaginas().size() : 0)
                .fechaPublicacion(capitulo.getFechaPublicacion())
                .build();
    }
}