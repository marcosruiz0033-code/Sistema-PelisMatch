package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.repository.SerieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class SerieService {

    private static final int CANTIDAD_ADICIONALES = 12;

    private final SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public List<Serie> listarTodas() {
        return serieRepository.findAllByOrderByIdSerieDesc();
    }

    public Page<Serie> listarPagina(int pagina, int tamanio) {
        Pageable pageable = PageRequest.of(Math.max(0, pagina), tamanio);
        Page<Serie> page = serieRepository.findPageByOrderByIdSerieDesc(pageable);
        if (page.getTotalPages() > 0 && page.getNumber() >= page.getTotalPages()) {
            page = serieRepository.findPageByOrderByIdSerieDesc(
                    PageRequest.of(page.getTotalPages() - 1, tamanio));
        }
        return page;
    }

    public Page<Serie> buscarPagina(String genero, String orden, int pagina, int tamanio) {
        Sort sort = ordenamiento(orden);
        String filtroGenero = genero == null || genero.isBlank() ? null : genero.trim();
        int paginaSolicitada = Math.max(0, pagina);
        Page<Serie> page = serieRepository.buscarPagina(
                filtroGenero, PageRequest.of(paginaSolicitada, tamanio, sort));
        if (page.getTotalPages() == 0 && paginaSolicitada > 0) {
            return serieRepository.buscarPagina(filtroGenero, PageRequest.of(0, tamanio, sort));
        }
        if (page.getTotalPages() > 0 && paginaSolicitada >= page.getTotalPages()) {
            return serieRepository.buscarPagina(filtroGenero,
                    PageRequest.of(page.getTotalPages() - 1, tamanio, sort));
        }
        return page;
    }

    public List<Serie> listarRecientes() {
        return serieRepository.findTop5ByOrderByIdSerieDesc();
    }

    public List<String> generos() {
        return serieRepository.findAll().stream()
                .map(serie -> serie.getCategoria() == null ? null : serie.getCategoria().getNombre())
                .filter(genero -> genero != null && !genero.isBlank())
                .map(String::trim)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public long contar() {
        return serieRepository.count();
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

    public Serie guardar(Serie serie) {
        return serieRepository.save(serie);
    }

    private Sort ordenamiento(String orden) {
        String criterio = orden == null || orden.isBlank()
                ? "populares"
                : orden.trim().toLowerCase(Locale.ROOT);

        return switch (criterio) {
            case "populares", "recientes" -> Sort.by(
                    Sort.Order.desc("anio").nullsLast(),
                    Sort.Order.desc("idSerie"));
            case "antiguas" -> Sort.by(
                    Sort.Order.asc("anio").nullsLast(),
                    Sort.Order.asc("idSerie"));
            case "az" -> Sort.by(
                    Sort.Order.asc("titulo").ignoreCase(),
                    Sort.Order.asc("idSerie"));
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El criterio de orden no es válido");
        };
    }

    public void eliminar(Integer id) {
        serieRepository.deleteById(id);
    }

}
