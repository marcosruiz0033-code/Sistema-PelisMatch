package com.pelisMatch.movie_catalog.repository;

import com.pelisMatch.movie_catalog.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    List<Pelicula> findByGeneroIgnoreCase(String genero);

    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);
}