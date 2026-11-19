package com.pelisMatch.movie_catalog.service;

<<<<<<< HEAD
import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculaServiceTest {

    @Mock
    private PeliculaRepository repository;

    @InjectMocks
    private PeliculaService peliculaService;

    private Pelicula pelicula(Long id, String titulo) {
        Pelicula p = new Pelicula();
        p.setId(id);
        p.setTitulo(titulo);
        return p;
    }

    @Test
    void listarPagina_devuelveLaPaginaSolicitada() {
        Page<Pelicula> page = new PageImpl<>(
                List.of(pelicula(1L, "Uno")),
                PageRequest.of(1, 14), 20);
        when(repository.findPageByOrderByIdDesc(any())).thenReturn(page);

        Page<Pelicula> resultado = peliculaService.listarPagina(1, 14);

        assertThat(resultado.getNumber()).isEqualTo(1);
        assertThat(resultado.getTotalPages()).isEqualTo(2);
        assertThat(resultado.getContent()).hasSize(1);
    }

    @Test
    void listarPagina_fueraDeRango_caeEnLaUltimaPagina() {
        Page<Pelicula> fueraDeRango = new PageImpl<>(
                List.of(), PageRequest.of(50, 14), 20);
        Page<Pelicula> ultimaPagina = new PageImpl<>(
                List.of(pelicula(20L, "Ultima")), PageRequest.of(1, 14), 20);
        when(repository.findPageByOrderByIdDesc(any()))
                .thenReturn(fueraDeRango, ultimaPagina);

        Page<Pelicula> resultado = peliculaService.listarPagina(50, 14);

        assertThat(resultado.getNumber()).isEqualTo(1);
        verify(repository, times(2)).findPageByOrderByIdDesc(any());
    }

    @Test
    void buscarPorTitulo_filtraPorCoincidenciaParcial() {
        when(repository.findByTituloContainingIgnoreCase("dark"))
                .thenReturn(List.of(pelicula(1L, "Dark")));

        assertThat(peliculaService.buscarPorTitulo("dark")).hasSize(1);
    }

    @Test
    void obtenerPorId_lanzaErrorSiNoExiste() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> peliculaService.obtenerPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    @Test
    void eliminar_borraLaPelicula() {
        peliculaService.eliminar(5L);
        verify(repository).deleteById(5L);
    }

    @Test
    void contar_devuelveElTotal() {
        when(repository.count()).thenReturn(20L);

        assertThat(peliculaService.contar()).isEqualTo(20);
    }

=======
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
>>>>>>> origin/modulo-peliculas
}
