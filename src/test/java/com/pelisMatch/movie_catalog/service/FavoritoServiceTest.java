package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Favorito;
import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.model.Usuario;
import com.pelisMatch.movie_catalog.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritoServiceTest {

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private PeliculaRepository peliculaRepository;

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private FavoritoService favoritoService;

    private static final Integer ID_USUARIO = 1;

    @Test
    void alternarPelicula_agregaSiNoExiste() {
        when(favoritoRepository.findByUsuarioIdUsuarioAndPeliculaId(ID_USUARIO, 10L))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findById(ID_USUARIO))
                .thenReturn(Optional.of(new Usuario()));
        when(peliculaRepository.findById(10L))
                .thenReturn(Optional.of(new Pelicula()));

        boolean agregado = favoritoService.alternarPelicula(ID_USUARIO, 10L);

        assertThat(agregado).isTrue();
        verify(favoritoRepository).save(any(Favorito.class));
        verify(favoritoRepository, never()).delete(any());
    }

    @Test
    void alternarPelicula_quitaSiYaExiste() {
        Favorito existente = new Favorito();
        when(favoritoRepository.findByUsuarioIdUsuarioAndPeliculaId(ID_USUARIO, 10L))
                .thenReturn(Optional.of(existente));

        boolean agregado = favoritoService.alternarPelicula(ID_USUARIO, 10L);

        assertThat(agregado).isFalse();
        verify(favoritoRepository).delete(existente);
        verify(favoritoRepository, never()).save(any());
    }

    @Test
    void alternarSerie_agregaSiNoExiste() {
        when(favoritoRepository.findByUsuarioIdUsuarioAndSerieIdSerie(ID_USUARIO, 5))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findById(ID_USUARIO))
                .thenReturn(Optional.of(new Usuario()));
        when(serieRepository.findById(5))
                .thenReturn(Optional.of(new Serie()));

        boolean agregado = favoritoService.alternarSerie(ID_USUARIO, 5);

        assertThat(agregado).isTrue();
        verify(favoritoRepository).save(any(Favorito.class));
    }

    @Test
    void alternarSerie_quitaSiYaExiste() {
        Favorito existente = new Favorito();
        when(favoritoRepository.findByUsuarioIdUsuarioAndSerieIdSerie(ID_USUARIO, 5))
                .thenReturn(Optional.of(existente));

        boolean agregado = favoritoService.alternarSerie(ID_USUARIO, 5);

        assertThat(agregado).isFalse();
        verify(favoritoRepository).delete(existente);
    }

    @Test
    void idsFavoritos_devuelvePeliculasYSeriesPorSeparado() {
        Pelicula pelicula = new Pelicula();
        pelicula.setId(7L);
        Serie serie = new Serie();
        serie.setIdSerie(3);

        Favorito favPeli = new Favorito();
        favPeli.setPelicula(pelicula);
        Favorito favSerie = new Favorito();
        favSerie.setSerie(serie);

        when(favoritoRepository.findByUsuarioIdUsuarioOrderByIdFavoritoDesc(ID_USUARIO))
                .thenReturn(List.of(favPeli, favSerie));

        Map<String, List<Object>> ids = favoritoService.idsFavoritos(ID_USUARIO);

        assertThat(ids.get("peliculas")).containsExactly(7L);
        assertThat(ids.get("series")).containsExactly(3);
    }

    @Test
    void peliculasFavoritas_devuelveSoloPeliculas() {
        Pelicula pelicula = new Pelicula();
        pelicula.setId(7L);
        Favorito favPeli = new Favorito();
        favPeli.setPelicula(pelicula);
        Favorito favSerie = new Favorito();
        favSerie.setSerie(new Serie());

        when(favoritoRepository.findByUsuarioIdUsuarioOrderByIdFavoritoDesc(ID_USUARIO))
                .thenReturn(List.of(favPeli, favSerie));

        assertThat(favoritoService.peliculasFavoritas(ID_USUARIO)).hasSize(1);
        assertThat(favoritoService.seriesFavoritas(ID_USUARIO)).hasSize(1);
    }

}
