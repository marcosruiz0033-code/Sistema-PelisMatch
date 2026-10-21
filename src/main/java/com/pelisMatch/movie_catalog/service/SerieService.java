package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.repository.SerieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {

    private final SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public List<Serie> listarTodas() {
        return serieRepository.findAll();
    }

    public List<Serie> listarRecientes() {
        return serieRepository.findTop6ByOrderByIdSerieDesc();
    }

    public Optional<Serie> buscarPorId(Integer id) {
        return serieRepository.findById(id);
    }

    public List<Serie> buscarPorTitulo(String titulo) {
        return serieRepository.findByTituloContainingIgnoreCase(titulo);
    }

}
