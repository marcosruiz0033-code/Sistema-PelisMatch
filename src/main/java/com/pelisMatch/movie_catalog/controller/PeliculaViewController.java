package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.service.PeliculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/peliculas")
public class PeliculaViewController {

    private final PeliculaService service;

    public PeliculaViewController(PeliculaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q,
                         @RequestParam(required = false) String genero,
                         Model model) {
        model.addAttribute("peliculas", service.buscar(q, genero));
        model.addAttribute("masValoradas", service.masValoradas(5));
        model.addAttribute("generos", service.generos());
        model.addAttribute("q", q);
        model.addAttribute("generoActual", genero);
        return "peliculas/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Pelicula> encontrada = service.buscarPorId(id);
        if (encontrada.isEmpty()) {
            return "redirect:/peliculas";
        }
        Pelicula pelicula = encontrada.get();
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("relacionadas", service.relacionadas(pelicula, 4));
        model.addAttribute("generos", service.generos());
        return "peliculas/detalle";
    }
}
