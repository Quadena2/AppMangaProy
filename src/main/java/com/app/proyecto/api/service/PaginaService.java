package com.app.proyecto.api.service;

import com.app.proyecto.api.dto.response.PaginaResponse;
import com.app.proyecto.api.entity.Capitulo;
import com.app.proyecto.api.entity.Pagina;
import com.app.proyecto.api.repository.CapituloRepository;
import com.app.proyecto.api.repository.PaginaRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaginaService {

    private final PaginaRepository paginaRepository;
    private final CapituloRepository capituloRepository;
    private final Cloudinary cloudinary;

    public List<PaginaResponse> listarPorCapitulo(Long capituloId) {
        return paginaRepository.findByCapituloIdOrderByNumeroAsc(capituloId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PaginaResponse subirPagina(Long capituloId,
                                      Integer numero,
                                      MultipartFile imagen,
                                      String emailAutor) {
        Capitulo capitulo = capituloRepository.findById(capituloId)
                .orElseThrow(() -> new RuntimeException("Capítulo no encontrado"));

        if (!capitulo.getTitulo().getAutor().getEmail().equals(emailAutor)) {
            throw new RuntimeException("No tienes permiso para subir páginas a este capítulo");
        }

        String imagenUrl = subirImagen(imagen, "paginas");

        Pagina pagina = Pagina.builder()
                .capitulo(capitulo)
                .numero(numero)
                .imagenUrl(imagenUrl)
                .build();

        return toResponse(paginaRepository.save(pagina));
    }

    private String subirImagen(MultipartFile file, String carpeta) {
        try {
            Map resultado = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "manga-app/" + carpeta)
            );
            return (String) resultado.get("secure_url");
        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen: " + e.getMessage());
        }
    }

    private PaginaResponse toResponse(Pagina pagina) {
        return PaginaResponse.builder()
                .id(pagina.getId())
                .numero(pagina.getNumero())
                .imagenUrl(pagina.getImagenUrl())
                .capituloId(pagina.getCapitulo().getId())
                .build();
    }
}