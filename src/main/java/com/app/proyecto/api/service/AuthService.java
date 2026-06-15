package com.app.proyecto.api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.app.proyecto.api.dto.request.GoogleAuthRequest;
import com.app.proyecto.api.dto.request.LoginRequest;
import com.app.proyecto.api.dto.request.RegisterRequest;
import com.app.proyecto.api.dto.response.AuthResponse;
import com.app.proyecto.api.entity.Usuario;
import com.app.proyecto.api.enums.Rol;
import com.app.proyecto.api.repository.UsuarioRepository;
import com.app.proyecto.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.client.id}")
    private String googleClientId;

    // Registro con email
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Validar que USER no pueda auto-asignarse ADMIN
        Rol rol = request.getRol();
        if (rol == Rol.ADMIN) {
            throw new RuntimeException("No puedes registrarte como administrador");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(rol)
                .provider("EMAIL")
                .build();

        usuarioRepository.save(usuario);

        String jwt = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());

        return AuthResponse.builder()
                .jwt(jwt)
                .rol(usuario.getRol().name())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .fotoPerfil(null)
                .build();
    }

    // ── Login con email
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));

        if (usuario.getProvider().equals("GOOGLE")) {
            throw new RuntimeException("Esta cuenta usa Google para iniciar sesión");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        String jwt = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());

        return AuthResponse.builder()
                .jwt(jwt)
                .rol(usuario.getRol().name())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .fotoPerfil(usuario.getFotoPerfil())
                .build();
    }

    // ── Login con Google
    public AuthResponse authenticateWithGoogle(GoogleAuthRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getIdToken());

            if (idToken == null) {
                throw new RuntimeException("Token de Google inválido");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String nombre = (String) payload.get("name");
            String fotoPerfil = (String) payload.get("picture");

            Usuario usuario = usuarioRepository.findByGoogleId(googleId)
                    .orElseGet(() -> {
                        // Si ya existe con ese email (registrado con email antes)
                        return usuarioRepository.findByEmail(email)
                                .orElseGet(() -> {
                                    Usuario nuevo = Usuario.builder()
                                            .googleId(googleId)
                                            .email(email)
                                            .nombre(nombre)
                                            .fotoPerfil(fotoPerfil)
                                            .rol(Rol.USER)
                                            .provider("GOOGLE")
                                            .build();
                                    return usuarioRepository.save(nuevo);
                                });
                    });

            usuario.setNombre(nombre);
            usuario.setFotoPerfil(fotoPerfil);
            if (usuario.getGoogleId() == null) {
                usuario.setGoogleId(googleId);
            }
            usuarioRepository.save(usuario);

            String jwt = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());

            return AuthResponse.builder()
                    .jwt(jwt)
                    .rol(usuario.getRol().name())
                    .nombre(usuario.getNombre())
                    .email(usuario.getEmail())
                    .fotoPerfil(usuario.getFotoPerfil())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error al autenticar con Google: " + e.getMessage());
        }
    }
}