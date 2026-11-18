package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Categoria;
import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeliculaServiceTest {

    private final PeliculaRepository repository = mock(PeliculaRepository.class);
    private final PeliculaService service = new PeliculaService(repository);

    @Test
    void filtraPorCategoriaYOrdenaPorPopularidad() {
        when(repository.findAll()).thenReturn(List.of(
                pelicula(1L, "Terror 7", "Terror", 2018, 7.0),
                pelicula(2L, "Drama", "Drama", 2024, 9.8),
                pelicula(3L, "Terror 9", "Terror", 2020, 9.0)
        ));

        assertThat(service.buscar(null, "Terror", "populares"))
                .extracting(Pelicula::getTitulo)
                .containsExactly("Terror 9", "Terror 7");
    }

    @Test
    void ordenaPorAnioYTituloSegunElCriterioElegido() {
        when(repository.findAll()).thenReturn(List.of(
                pelicula(1L, "Zorro", "Drama", 2010, 8.0),
                pelicula(2L, "Aventura", "Drama", 2020, 7.0),
                pelicula(3L, "Medio", "Drama", 2000, 9.0)
        ));

        assertThat(service.buscar(null, null, "recientes"))
                .extracting(Pelicula::getTitulo)
                .containsExactly("Aventura", "Zorro", "Medio");
        assertThat(service.buscar(null, null, "antiguas"))
                .extracting(Pelicula::getTitulo)
                .containsExactly("Medio", "Zorro", "Aventura");
        assertThat(service.buscar(null, null, "az"))
                .extracting(Pelicula::getTitulo)
                .containsExactly("Aventura", "Medio", "Zorro");
    }

    private Pelicula pelicula(Long id, String titulo, String genero, int anio, double puntuacion) {
        Categoria categoria = new Categoria();
        categoria.setNombre(genero);

        Pelicula pelicula = new Pelicula();
        pelicula.setId(id);
        pelicula.setTitulo(titulo);
        pelicula.setCategoria(categoria);
        pelicula.setAnio(anio);
        pelicula.setPuntuacion(puntuacion);
        return pelicula;
    }
}
