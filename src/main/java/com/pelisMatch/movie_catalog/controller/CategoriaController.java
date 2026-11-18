package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Categoria;
import com.pelisMatch.movie_catalog.repository.SerieRepository;
import com.pelisMatch.movie_catalog.service.CategoriaService;
import com.pelisMatch.movie_catalog.service.PeliculaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.Locale;

@Controller
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final PeliculaService peliculaService;
    private final SerieRepository serieRepository;

    public CategoriaController(CategoriaService categoriaService,
                               PeliculaService peliculaService,
                               SerieRepository serieRepository) {
        this.categoriaService = categoriaService;
        this.peliculaService = peliculaService;
        this.serieRepository = serieRepository;
    }

    @GetMapping("/categorias/{slug}")
    public String listarPorCategoria(@PathVariable String slug, Model model) {
        String nombreCategoria = categoriaService.listarTodas().stream()
                .map(Categoria::getNombre)
                .filter(nombre -> crearSlug(nombre).equals(crearSlug(slug)))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la categoría solicitada"));

        model.addAttribute("consulta", nombreCategoria);
        model.addAttribute("peliculas", peliculaService.listarPorGenero(nombreCategoria));
        model.addAttribute("series", serieRepository.findByCategoriaNombreIgnoreCase(nombreCategoria));
        model.addAttribute("paginaActiva", "");
        return "usuario/Search";
    }

    private String crearSlug(String valor) {
        String sinAcentos = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return sinAcentos.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-)|(-$)", "");
    }
}
