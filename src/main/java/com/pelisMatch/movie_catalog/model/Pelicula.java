package com.pelisMatch.movie_catalog.model;

public class Pelicula {

    private Long id;
    private String titulo;
    private String genero;
    private String director;
    private int anio;
    private String descripcion;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

