package com.pelisMatch.movie_catalog.service;

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

}
