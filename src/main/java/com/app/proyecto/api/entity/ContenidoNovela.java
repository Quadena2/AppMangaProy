package com.app.proyecto.api.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cotenido_novela")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContenidoNovela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capitulo_id", nullable = false)
    private Capitulo capitulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String texto;
}
