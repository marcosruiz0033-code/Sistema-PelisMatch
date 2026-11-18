package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Favorito;
import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.model.Usuario;
import com.pelisMatch.movie_catalog.repository.FavoritoRepository;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;
import com.pelisMatch.movie_catalog.repository.SerieRepository;
import com.pelisMatch.movie_catalog.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final PeliculaRepository peliculaRepository;
    private final SerieRepository serieRepository;
    private final UsuarioRepository usuarioRepository;

    public FavoritoService(FavoritoRepository favoritoRepository,
                           PeliculaRepository peliculaRepository,
                           SerieRepository serieRepository,
                           UsuarioRepository usuarioRepository) {
        this.favoritoRepository = favoritoRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public boolean alternarPelicula(Integer idUsuario, Long idPelicula) {
        Optional<Favorito> existente = favoritoRepository.findByUsuarioIdUsuarioAndPeliculaId(idUsuario, idPelicula);
        if (existente.isPresent()) {
            favoritoRepository.delete(existente.get());
            return false;
        }
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Pelicula pelicula = peliculaRepository.findById(idPelicula)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setPelicula(pelicula);
        favoritoRepository.save(favorito);
        return true;
    }

    @Transactional
    public boolean alternarSerie(Integer idUsuario, Integer idSerie) {
        Optional<Favorito> existente = favoritoRepository.findByUsuarioIdUsuarioAndSerieIdSerie(idUsuario, idSerie);
        if (existente.isPresent()) {
            favoritoRepository.delete(existente.get());
            return false;
        }
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Serie serie = serieRepository.findById(idSerie)
                .orElseThrow(() -> new RuntimeException("Serie no encontrada"));
        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setSerie(serie);
        favoritoRepository.save(favorito);
        return true;
    }

    public Map<String, List<Object>> idsFavoritos(Integer idUsuario) {
        List<Object> peliculas = favoritoRepository.findByUsuarioIdUsuarioOrderByIdFavoritoDesc(idUsuario).stream()
                .filter(f -> f.getPelicula() != null)
                .map(f -> (Object) f.getPelicula().getId())
                .toList();
        List<Object> series = favoritoRepository.findByUsuarioIdUsuarioOrderByIdFavoritoDesc(idUsuario).stream()
                .filter(f -> f.getSerie() != null)
                .map(f -> (Object) f.getSerie().getIdSerie())
                .toList();
        return Map.of("peliculas", peliculas, "series", series);
    }

    public List<Pelicula> peliculasFavoritas(Integer idUsuario) {
        return favoritoRepository.findByUsuarioIdUsuarioOrderByIdFavoritoDesc(idUsuario).stream()
                .filter(f -> f.getPelicula() != null)
                .map(Favorito::getPelicula)
                .toList();
    }

    public List<Serie> seriesFavoritas(Integer idUsuario) {
        return favoritoRepository.findByUsuarioIdUsuarioOrderByIdFavoritoDesc(idUsuario).stream()
                .filter(f -> f.getSerie() != null)
                .map(Favorito::getSerie)
                .toList();
    }

}
