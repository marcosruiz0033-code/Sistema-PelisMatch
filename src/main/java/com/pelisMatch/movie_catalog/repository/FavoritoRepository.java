package com.pelisMatch.movie_catalog.repository;

import com.pelisMatch.movie_catalog.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {

    Optional<Favorito> findByUsuarioIdUsuarioAndPeliculaId(Integer idUsuario, Long idPelicula);

    Optional<Favorito> findByUsuarioIdUsuarioAndSerieIdSerie(Integer idUsuario, Integer idSerie);

    List<Favorito> findByUsuarioIdUsuarioOrderByIdFavoritoDesc(Integer idUsuario);

}
