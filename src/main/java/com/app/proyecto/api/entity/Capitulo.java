package com.app.proyecto.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "capitulos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Capitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titulo_id", nullable = false)
    private Titulo titulo;

    @Column(nullable = false)
    private Integer numero;

    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaPublicacion;

    @OneToMany(mappedBy = "capitulo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numero ASC")
    private List<Pagina> paginas;

    @OneToOne(mappedBy = "capitulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private ContenidoNovela contenidoNovela;

    @PrePersist
    public void prePersist() {
        fechaPublicacion = LocalDateTime.now();
    }
}