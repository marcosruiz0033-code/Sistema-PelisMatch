package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PeliculaService {

    private final PeliculaRepository repository;

    public PeliculaService(PeliculaRepository repository) {
        this.repository = repository;
    }

    public Pelicula obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la película con id " + id));
    }

    public List<Pelicula> listar() {
        return repository.findAllByOrderByIdDesc();
    }

    public Page<Pelicula> listarPagina(int pagina, int tamanio) {
        Pageable pageable = PageRequest.of(Math.max(0, pagina), tamanio);
        Page<Pelicula> page = repository.findPageByOrderByIdDesc(pageable);
        if (page.getTotalPages() > 0 && page.getNumber() >= page.getTotalPages()) {
            page = repository.findPageByOrderByIdDesc(
                    PageRequest.of(page.getTotalPages() - 1, tamanio));
        }
        return page;
    }

    public Page<Pelicula> buscarPagina(String q, String genero, String orden,
                                       int pagina, int tamanio) {
        Sort sort = ordenamiento(orden);
        String texto = q == null || q.isBlank() ? null : q.trim();
        String filtroGenero = genero == null || genero.isBlank() ? null : genero.trim();
        int paginaSolicitada = Math.max(0, pagina);
        Page<Pelicula> page = repository.buscarPagina(
                texto, filtroGenero, PageRequest.of(paginaSolicitada, tamanio, sort));
        if (page.getTotalPages() == 0 && paginaSolicitada > 0) {
            return repository.buscarPagina(texto, filtroGenero, PageRequest.of(0, tamanio, sort));
        }
        if (page.getTotalPages() > 0 && paginaSolicitada >= page.getTotalPages()) {
            return repository.buscarPagina(texto, filtroGenero,
                    PageRequest.of(page.getTotalPages() - 1, tamanio, sort));
        }
        return page;
    }

    public List<Pelicula> listarRecientes() {
        return repository.findTop5ByOrderByIdDesc();
    }

    public long contar() {
        return repository.count();
    }

    public List<Pelicula> listarPorGenero(String genero) {
        return repository.findByCategoriaNombreIgnoreCase(genero);
    }

    public List<Pelicula> buscarPorTitulo(String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
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

    /** Busca por texto en el título y/o filtra por género. Los más nuevos primero. */
    public List<Pelicula> buscar(String q, String genero, String orden) {
        String texto = q == null ? "" : q.trim().toLowerCase(Locale.ROOT);
        String filtroGenero = genero == null ? "" : genero.trim();
        return repository.findAll().stream()
                .filter(p -> texto.isEmpty()
                        || (p.getTitulo() != null
                        && p.getTitulo().toLowerCase(Locale.ROOT).contains(texto)))
                .filter(p -> filtroGenero.isEmpty()
                        || filtroGenero.equalsIgnoreCase(obtenerGenero(p)))
                .sorted(comparador(orden))
                .toList();
    }

    private Comparator<Pelicula> comparador(String orden) {
        String criterio = orden == null || orden.isBlank()
                ? "populares"
                : orden.trim().toLowerCase(Locale.ROOT);

        Comparator<Pelicula> porIdDescendente = Comparator.comparing(
                Pelicula::getId, Comparator.nullsLast(Comparator.reverseOrder()));

        return switch (criterio) {
            case "populares" -> Comparator.comparing(
                    Pelicula::getPuntuacion, Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(Pelicula::getAnio,
                            Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(porIdDescendente);
            case "recientes" -> Comparator.comparing(
                    Pelicula::getAnio, Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(porIdDescendente);
            case "antiguas" -> Comparator.comparing(
                    Pelicula::getAnio, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Pelicula::getId, Comparator.nullsLast(Comparator.naturalOrder()));
            case "az" -> Comparator.comparing(
                    Pelicula::getTitulo, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Pelicula::getId, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El criterio de orden no es válido");
        };
    }

    private Sort ordenamiento(String orden) {
        String criterio = orden == null || orden.isBlank()
                ? "populares"
                : orden.trim().toLowerCase(Locale.ROOT);
        return switch (criterio) {
            case "populares" -> Sort.by(
                    Sort.Order.desc("puntuacion").nullsLast(),
                    Sort.Order.desc("anio").nullsLast(),
                    Sort.Order.desc("id"));
            case "recientes" -> Sort.by(
                    Sort.Order.desc("anio").nullsLast(),
                    Sort.Order.desc("id"));
            case "antiguas" -> Sort.by(
                    Sort.Order.asc("anio").nullsLast(),
                    Sort.Order.asc("id"));
            case "az" -> Sort.by(
                    Sort.Order.asc("titulo").ignoreCase(),
                    Sort.Order.asc("id"));
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El criterio de orden no es válido");
        };
    }

    private String obtenerGenero(Pelicula pelicula) {
        if (pelicula.getCategoria() != null && pelicula.getCategoria().getNombre() != null) {
            return pelicula.getCategoria().getNombre();
        }
        return pelicula.getGenero();
    }

    /** Las películas con mejor puntuación (para la barra lateral). */
    public List<Pelicula> masValoradas(int cantidad) {
        return repository.findAll().stream()
                .filter(p -> p.getPuntuacion() != null)
                .sorted(Comparator.comparing(Pelicula::getPuntuacion).reversed())
                .limit(cantidad)
                .toList();
    }

    /** Lista de géneros sin repetir, para el menú. */
    public List<String> generos() {
        return repository.findAll().stream()
                .map(this::obtenerGenero)
                .filter(genero -> genero != null && !genero.isBlank())
                .map(String::trim)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    /** Otras películas del mismo género que la indicada. */
    public List<Pelicula> relacionadas(Pelicula base, int cantidad) {
        return repository.findAll().stream()
                .filter(p -> !p.getId().equals(base.getId()))
                .filter(p -> base.getGenero() != null && base.getGenero().equalsIgnoreCase(p.getGenero()))
                .limit(cantidad)
                .toList();
    }
}
