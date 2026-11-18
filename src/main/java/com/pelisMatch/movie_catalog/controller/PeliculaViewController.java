package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.service.PeliculaService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PeliculaViewController {

    private static final int TAMANIO_PAGINA = 14;

    private final PeliculaService service;

    public PeliculaViewController(PeliculaService service) {
        this.service = service;
    }

    @GetMapping("/peliculas")
    public String listar(@RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<Pelicula> page = service.listarPagina(pagina, TAMANIO_PAGINA);
        model.addAttribute("peliculas", page.getContent());
        model.addAttribute("paginaActual", page.getNumber());
        model.addAttribute("totalPaginas", page.getTotalPages());
        model.addAttribute("paginaActiva", "peliculas");
        return "peliculas/lista";
    }

    @GetMapping("/peliculas/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        return service.buscarPorId(id)
                .map(pelicula -> {
                    model.addAttribute("pelicula", pelicula);
                    model.addAttribute("paginaActiva", "peliculas");
                    return "peliculas/detalle";
                })
                .orElse("redirect:/peliculas");
    }
}