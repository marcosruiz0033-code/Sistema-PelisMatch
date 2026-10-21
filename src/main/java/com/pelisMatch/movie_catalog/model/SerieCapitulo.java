package com.pelisMatch.movie_catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "serie_capitulo")
public class SerieCapitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCapitulo;

    @NotNull(message = "La temporada es obligatoria")
    @Column(nullable = false)
    private Integer temporada;

    @NotNull(message = "El número de capítulo es obligatorio")
    @Column(nullable = false)
    private Integer numeroCapitulo;

    @Column(length = 150)
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 1000)
    private String descripcion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_serie", nullable = false)
    private Serie serie;

}
