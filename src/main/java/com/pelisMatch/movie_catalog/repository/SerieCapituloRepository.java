package com.pelisMatch.movie_catalog.repository;

import com.pelisMatch.movie_catalog.model.SerieCapitulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SerieCapituloRepository extends JpaRepository<SerieCapitulo, Integer> {

    List<SerieCapitulo> findBySerieIdSerieOrderByTemporadaAscNumeroCapituloAsc(Integer idSerie);

}
