package com.pelisMatch.movie_catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "peliculas")
public class Pelicula extends ContenidoBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String director;

    private Integer duracion;
    private String genero;
    private Double puntuacion;
    private String descripcion;

    @Column(length = 2000)
    private String reparto;

    public Pelicula() {
    }

}
