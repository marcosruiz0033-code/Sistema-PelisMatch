package com.pelisMatch.movie_catalog.repository;

import com.pelisMatch.movie_catalog.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Integer> {

    List<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findTop4ByOrderByIdSerieDesc();

    List<Serie> findAllByOrderByIdSerieDesc(Pageable pageable);

    List<Serie> findAllByOrderByIdSerieDesc();

    List<Serie> findTop5ByOrderByIdSerieDesc();

    List<Serie> findByIdSerieNotInOrderByIdSerieDesc(List<Integer> ids, Pageable pageable);

    Page<Serie> findPageByOrderByIdSerieDesc(Pageable pageable);

    @Query(value = """
            SELECT s FROM Serie s LEFT JOIN s.categoria c
            WHERE (:genero IS NULL OR :genero = '' OR LOWER(c.nombre) = LOWER(:genero))
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Serie s LEFT JOIN s.categoria c
                    WHERE (:genero IS NULL OR :genero = '' OR LOWER(c.nombre) = LOWER(:genero))
                    """)
    Page<Serie> buscarPagina(@Param("genero") String genero, Pageable pageable);

    List<Serie> findByCategoriaNombreIgnoreCase(String nombre);

}
