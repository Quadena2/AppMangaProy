package com.app.proyecto.api.entity;

import com.app.proyecto.api.enums.EstadoTitulo;
import com.app.proyecto.api.enums.TipoTitulo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "titulos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTitulo tipo;

    private String portadaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTitulo estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @OneToMany(mappedBy = "titulo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numero ASC")
    private List<Capitulo> capitulos;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist(){
        fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = EstadoTitulo.EN_CURSO;
    }

}
