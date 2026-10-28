package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Usuario;
import com.pelisMatch.movie_catalog.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return repository.findByCorreoIgnoreCase(correo);
    }

    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    public long contar() {
        return repository.countByRolIgnoreCase("USER");
    }
}
