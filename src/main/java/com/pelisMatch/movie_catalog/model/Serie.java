package com.pelisMatch.movie_catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "serie")
public class Serie extends ContenidoBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSerie;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false, length = 30)
    private String estado;

    @Column
    private Integer temporadas;

}
