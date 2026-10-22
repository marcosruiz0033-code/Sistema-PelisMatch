package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

    /** Busca por texto en el título y/o filtra por género. Los más nuevos primero. */
    public List<Pelicula> buscar(String q, String genero) {
        String texto = q == null ? "" : q.trim().toLowerCase();
        boolean sinGenero = genero == null || genero.isBlank();
        return repository.findAll().stream()
                .filter(p -> texto.isEmpty()
                        || (p.getTitulo() != null && p.getTitulo().toLowerCase().contains(texto)))
                .filter(p -> sinGenero || genero.equalsIgnoreCase(p.getGenero()))
                .sorted(Comparator.comparing(Pelicula::getId).reversed())
                .toList();
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
                .map(Pelicula::getGenero)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
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
