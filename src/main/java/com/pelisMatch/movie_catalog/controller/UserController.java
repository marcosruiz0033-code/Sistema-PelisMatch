package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.service.PeliculaService;
import com.pelisMatch.movie_catalog.service.SerieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final SerieService serieService;
    private final PeliculaService peliculaService;

    public UserController(SerieService serieService, PeliculaService peliculaService) {
        this.serieService = serieService;
        this.peliculaService = peliculaService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Serie> destacadas = serieService.listarDestacadas();
        List<Pelicula> peliculas = peliculaService.listar();

        model.addAttribute("seriesDestacadas", destacadas);
        model.addAttribute("seriesAdicionales", serieService.listarAdicionales(destacadas));
        model.addAttribute("peliculasRecientes", sublista(peliculas, 0, 5));
        model.addAttribute("peliculasActualizadas", sublista(peliculas, 5, 10));
        model.addAttribute("estrenosTop", sublista(peliculas, 10, 15));
        model.addAttribute("paginaActiva", "inicio");
        return "usuario/Home";
    }

    private List<Pelicula> sublista(List<Pelicula> peliculas, int desde, int hasta) {
        if (desde >= peliculas.size()) {
            return List.of();
        }
        return peliculas.subList(desde, Math.min(hasta, peliculas.size()));
    }

}
