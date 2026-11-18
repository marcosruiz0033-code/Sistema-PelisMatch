package com.pelisMatch.movie_catalog.repository;

import com.pelisMatch.movie_catalog.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    long countByRolIgnoreCase(String rol);
}
