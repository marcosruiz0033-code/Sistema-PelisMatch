package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.service.PeliculaService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
@RequestMapping("/peliculas")
public class PeliculaViewController {

    private static final int TAMANIO_PAGINA = 14;

    private final PeliculaService service;

    public PeliculaViewController(PeliculaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q,
                         @RequestParam(required = false) String genero,
                         @RequestParam(defaultValue = "populares") String orden,
                         @RequestParam(defaultValue = "0") int pagina,
                         Model model) {

        Page<Pelicula> page = service.listarPagina(pagina, TAMANIO_PAGINA);

        model.addAttribute("peliculas", page.getContent());
        model.addAttribute("paginaActual", page.getNumber());
        model.addAttribute("totalPaginas", page.getTotalPages());

        model.addAttribute("masValoradas", service.masValoradas(5));
        model.addAttribute("generos", service.generos());
        model.addAttribute("q", q);
        model.addAttribute("generoActual", genero == null ? "" : genero);
        model.addAttribute("ordenActual", orden);
        model.addAttribute("paginaActiva", "peliculas");

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