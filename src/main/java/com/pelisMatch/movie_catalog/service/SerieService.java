package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.repository.SerieRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {

    private static final int CANTIDAD_ADICIONALES = 12;

    private final SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public List<Serie> listarTodas() {
        return serieRepository.findAll();
    }

    public List<Serie> listarDestacadas() {
        return serieRepository.findTop4ByOrderByIdSerieDesc();
    }

    public List<Serie> listarAdicionales(List<Serie> destacados) {
        List<Integer> ids = destacados.stream().map(Serie::getIdSerie).toList();
        Pageable pageable = PageRequest.of(0, CANTIDAD_ADICIONALES);
        if (ids.isEmpty()) {
            return serieRepository.findAllByOrderByIdSerieDesc(pageable);
        }
        return serieRepository.findByIdSerieNotInOrderByIdSerieDesc(ids, pageable);
    }

    public Optional<Serie> buscarPorId(Integer id) {
        return serieRepository.findById(id);
    }

    public List<Serie> buscarPorTitulo(String titulo) {
        return serieRepository.findByTituloContainingIgnoreCase(titulo);
    }

}
