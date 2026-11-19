package com.pelisMatch.movie_catalog.repository;

import com.pelisMatch.movie_catalog.model.Pelicula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    List<Pelicula> findAllByOrderByIdDesc();

    List<Pelicula> findTop5ByOrderByIdDesc();

    List<Pelicula> findByCategoriaNombreIgnoreCase(String nombre);

    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);

    Page<Pelicula> findPageByOrderByIdDesc(Pageable pageable);

    @Query(value = """
            SELECT p FROM Pelicula p LEFT JOIN p.categoria c
            WHERE (:q IS NULL OR :q = '' OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :q, '%')))
              AND (:genero IS NULL OR :genero = '' OR LOWER(c.nombre) = LOWER(:genero)
                   OR (c IS NULL AND LOWER(p.genero) = LOWER(:genero)))
            """,
            countQuery = """
                    SELECT COUNT(p) FROM Pelicula p LEFT JOIN p.categoria c
                    WHERE (:q IS NULL OR :q = '' OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :q, '%')))
                      AND (:genero IS NULL OR :genero = '' OR LOWER(c.nombre) = LOWER(:genero)
                           OR (c IS NULL AND LOWER(p.genero) = LOWER(:genero)))
                    """)
    Page<Pelicula> buscarPagina(@Param("q") String q,
                                @Param("genero") String genero,
                                Pageable pageable);
}