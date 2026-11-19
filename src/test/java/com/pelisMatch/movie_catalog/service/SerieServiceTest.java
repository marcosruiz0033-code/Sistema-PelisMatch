package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.model.Categoria;
import com.pelisMatch.movie_catalog.repository.SerieRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SerieServiceTest {

    @Mock
    private SerieRepository serieRepository;

    @InjectMocks
    private SerieService serieService;

    private Serie serie(Integer id, String titulo) {
        Serie s = new Serie();
        s.setIdSerie(id);
        s.setTitulo(titulo);
        return s;
    }

    @Test
    void listarPagina_devuelveLaPaginaSolicitada() {
        Page<Serie> page = new PageImpl<>(
                List.of(serie(1, "Uno"), serie(2, "Dos")),
                PageRequest.of(0, 14), 30);
        when(serieRepository.findPageByOrderByIdSerieDesc(any())).thenReturn(page);

        Page<Serie> resultado = serieService.listarPagina(0, 14);

        assertThat(resultado.getContent()).hasSize(2);
        assertThat(resultado.getTotalPages()).isEqualTo(3);
        assertThat(resultado.getNumber()).isZero();
    }

    @Test
    void listarPagina_fueraDeRango_caeEnLaUltimaPagina() {
        Page<Serie> fueraDeRango = new PageImpl<>(
                List.of(), PageRequest.of(99, 14), 30);
        Page<Serie> ultimaPagina = new PageImpl<>(
                List.of(serie(30, "Ultima")), PageRequest.of(2, 14), 30);
        when(serieRepository.findPageByOrderByIdSerieDesc(any()))
                .thenReturn(fueraDeRango, ultimaPagina);

        Page<Serie> resultado = serieService.listarPagina(99, 14);

        assertThat(resultado.getNumber()).isEqualTo(2);
        verify(serieRepository, times(2)).findPageByOrderByIdSerieDesc(any());
    }

    @Test
    void listarPagina_numeroNegativo_seTrataComoPaginaCero() {
        Page<Serie> page = new PageImpl<>(
                List.of(serie(1, "Uno")), PageRequest.of(0, 14), 30);
        when(serieRepository.findPageByOrderByIdSerieDesc(any())).thenReturn(page);

        serieService.listarPagina(-5, 14);

        verify(serieRepository).findPageByOrderByIdSerieDesc(
                argThat(p -> p.getPageNumber() == 0 && p.getPageSize() == 14));
    }

    @Test
    void buscarPorId_devuelveLaSerieSiExiste() {
        when(serieRepository.findById(1)).thenReturn(Optional.of(serie(1, "Existe")));

        assertThat(serieService.buscarPorId(1)).isPresent();
        assertThat(serieService.buscarPorId(1).get().getTitulo()).isEqualTo("Existe");
    }

    @Test
    void buscarPorId_devuelveVacioSiNoExiste() {
        when(serieRepository.findById(9999)).thenReturn(Optional.empty());

        assertThat(serieService.buscarPorId(9999)).isEmpty();
    }

    @Test
    void contar_devuelveElTotalDeSeries() {
        when(serieRepository.count()).thenReturn(30L);

        assertThat(serieService.contar()).isEqualTo(30);
    }

    @Test
    void listarTodas_usaElRepositorio() {
        when(serieRepository.findAllByOrderByIdSerieDesc())
                .thenReturn(List.of(serie(1, "A"), serie(2, "B")));

        assertThat(serieService.listarTodas()).hasSize(2);
    }

    @Test
    void buscarPagina_filtraCategoriaYDevuelveLaPaginaValida() {
        Categoria cienciaFiccion = new Categoria();
        cienciaFiccion.setNombre("Ciencia ficción");
        Serie serieAntigua = serie(1, "Serie antigua");
        serieAntigua.setCategoria(cienciaFiccion);
        serieAntigua.setAnio(2010);
        Serie serieNueva = serie(2, "Serie nueva");
        serieNueva.setCategoria(cienciaFiccion);
        serieNueva.setAnio(2020);
        Page<Serie> fueraDeRango = new PageImpl<>(
                List.of(), PageRequest.of(99, 1), 2);
        Page<Serie> ultimaPagina = new PageImpl<>(
                List.of(serieAntigua), PageRequest.of(1, 1), 2);
        when(serieRepository.buscarPagina(eq("Ciencia ficción"), any()))
                .thenReturn(fueraDeRango, ultimaPagina);

        Page<Serie> resultado = serieService.buscarPagina("Ciencia ficción", "recientes", 99, 1);

        assertThat(resultado.getContent()).extracting(Serie::getTitulo)
                .containsExactly("Serie antigua");
        assertThat(resultado.getNumber()).isEqualTo(1);
        assertThat(resultado.getTotalPages()).isEqualTo(2);
        verify(serieRepository, times(2)).buscarPagina(eq("Ciencia ficción"), any());
    }

}
