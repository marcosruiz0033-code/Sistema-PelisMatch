package com.pelisMatch.movie_catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class ContenidoBase {

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "La sinopsis es obligatoria")
    @Column(length = 2000)
    private String sinopsis;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1888, message = "El año debe ser 1888 o posterior")
    @Max(value = 2100, message = "El año no puede ser posterior a 2100")
    @Column(nullable = false)
    private Integer anio;

    @Column(length = 500)
    private String imagen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(length = 500)
    private String urlTrailer;

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
