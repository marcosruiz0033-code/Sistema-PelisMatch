package com.pelisMatch.movie_catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    private String director;

    @Min(value = 1888, message = "El año no es válido")
    private int anio;

    @Size(max = 1000, message = "La descripción es muy larga")
    @Column(length = 1000)
    private String descripcion;

    
    private Integer duracion;          // minutos

    @Column(length = 500)
    private String reparto;            // "Actor 1, Actor 2, Actor 3"

    @Column(length = 500)
    private String posterUrl;          // URL de la imagen del póster

    private Double puntuacion;         // de 0 a 10

    public Pelicula() {
    }

    public Pelicula(Long id, String titulo, String genero, String director, int anio, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.director = director;
        this.anio = anio;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getDuracion() { return duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }

    public String getReparto() { return reparto; }
    public void setReparto(String reparto) { this.reparto = reparto; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public Double getPuntuacion() { return puntuacion; }
    public void setPuntuacion(Double puntuacion) { this.puntuacion = puntuacion; }
}
