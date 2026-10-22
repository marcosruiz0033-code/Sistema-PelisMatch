package com.pelisMatch.movie_catalog.repository;

import com.pelisMatch.movie_catalog.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNombre(String nombre);
}
