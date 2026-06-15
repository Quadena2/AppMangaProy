package com.app.proyecto.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paginas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capitulo_id", nullable = false)
    private Capitulo capitulo;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private String imagenUrl;
}
