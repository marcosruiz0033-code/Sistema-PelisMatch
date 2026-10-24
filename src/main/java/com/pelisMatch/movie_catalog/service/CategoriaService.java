package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Categoria;
import com.pelisMatch.movie_catalog.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listarTodas() {
        return repository.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return repository.findById(id);
    }

}
