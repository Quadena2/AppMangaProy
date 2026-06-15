package com.app.proyecto.api.service;

import com.app.proyecto.api.dto.request.TituloRequest;
import com.app.proyecto.api.dto.response.TituloResponse;
import com.app.proyecto.api.entity.Titulo;
import com.app.proyecto.api.entity.Usuario;
import com.app.proyecto.api.repository.TituloRepository;
import com.app.proyecto.api.repository.UsuarioRepository;
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
public class TituloService {

    private final TituloRepository tituloRepository;
    private final UsuarioRepository usuarioRepository;
    private final Cloudinary cloudinary;

    public List<TituloResponse> listarTodos() {
        return tituloRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TituloResponse obtenerPorId(Long id) {
        Titulo titulo = tituloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Título no encontrado"));
        return toResponse(titulo);
    }

    public List<TituloResponse> buscarPorNombre(String nombre) {
        return tituloRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TituloResponse> listarPorAutor(Long autorId) {
        return tituloRepository.findByAutorId(autorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TituloResponse crear(TituloRequest request,
                                MultipartFile portada,
                                String emailAutor) {
        Usuario autor = usuarioRepository.findByEmail(emailAutor)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String portadaUrl = null;
        if (portada != null && !portada.isEmpty()) {
            portadaUrl = subirImagen(portada, "portadas");
        }

        Titulo titulo = Titulo.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .tipo(request.getTipo())
                .estado(request.getEstado())
                .portadaUrl(portadaUrl)
                .autor(autor)
                .build();

        return toResponse(tituloRepository.save(titulo));
    }

    public TituloResponse actualizar(Long id,
                                     TituloRequest request,
                                     MultipartFile portada,
                                     String emailAutor) {
        Titulo titulo = tituloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Título no encontrado"));

        if (!titulo.getAutor().getEmail().equals(emailAutor)) {
            throw new RuntimeException("No tienes permiso para editar este título");
        }

        titulo.setNombre(request.getNombre());
        titulo.setDescripcion(request.getDescripcion());
        titulo.setEstado(request.getEstado());

        if (portada != null && !portada.isEmpty()) {
            titulo.setPortadaUrl(subirImagen(portada, "portadas"));
        }

        return toResponse(tituloRepository.save(titulo));
    }

    public void eliminar(Long id, String emailAutor) {
        Titulo titulo = tituloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Título no encontrado"));

        if (!titulo.getAutor().getEmail().equals(emailAutor)) {
            throw new RuntimeException("No tienes permiso para eliminar este título");
        }

        tituloRepository.delete(titulo);
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

    private TituloResponse toResponse(Titulo titulo) {
        return TituloResponse.builder()
                .id(titulo.getId())
                .nombre(titulo.getNombre())
                .descripcion(titulo.getDescripcion())
                .tipo(titulo.getTipo())
                .portadaUrl(titulo.getPortadaUrl())
                .estado(titulo.getEstado())
                .autorNombre(titulo.getAutor().getNombre())
                .autorId(titulo.getAutor().getId())
                .totalCapitulos(titulo.getCapitulos() != null ? titulo.getCapitulos().size() : 0)
                .fechaCreacion(titulo.getFechaCreacion())
                .build();
    }
}