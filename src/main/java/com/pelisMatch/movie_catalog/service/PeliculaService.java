package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PeliculaService {

    private final PeliculaRepository repository;

    public PeliculaService(PeliculaRepository repository) {
        this.repository = repository;
    }

    public List<Pelicula> listar() {
        return repository.findAll();
    }

    public List<Pelicula> listarPorGenero(String genero) {
        return repository.findByGeneroIgnoreCase(genero);
    }

    public Optional<Pelicula> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Pelicula guardar(Pelicula pelicula) {
        return repository.save(pelicula);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}