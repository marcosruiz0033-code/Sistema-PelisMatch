package com.pelisMatch.movie_catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "serie")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSerie;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "La sinopsis es obligatoria")
    @Column(nullable = false, length = 2000)
    private String sinopsis;

    @NotNull(message = "El año es obligatorio")
    @Column(nullable = false)
    private Integer anio;

    @Column(length = 500)
    private String imagen;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false, length = 30)
    private String estado;

    @Column
    private Integer temporadas;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(length = 500)
    private String urlNetflix;

    @Column(length = 500)
    private String urlPrimeVideo;

    @Column(length = 500)
    private String urlDisneyPlus;

    @Column(length = 500)
    private String urlMax;

    @Column(length = 500)
    private String urlAppleTv;

}
