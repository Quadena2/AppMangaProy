package com.app.proyecto.api.entity;

import com.app.proyecto.api.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Puede ser null si se registró con email
    @Column(unique = true)
    private String googleId;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    // Puede ser null si se registró con Google
    private String password;

    private String fotoPerfil;

    // "GOOGLE" o "EMAIL"
    @Column(nullable = false)
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Titulo> titulos;

    @PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
        if (rol == null) rol = Rol.USER;
        if (provider == null) provider = "EMAIL";
    }
}