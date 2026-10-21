package com.pelisMatch.movie_catalog.repository;

import com.pelisMatch.movie_catalog.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Integer> {

    List<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findTop6ByOrderByIdSerieDesc();

}
