package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.service.PeliculaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PeliculaViewController {

    private final PeliculaService service;

    public PeliculaViewController(PeliculaService service) {
        this.service = service;
    }

    @GetMapping("/peliculas")
    public String listar(Model model) {
        model.addAttribute("peliculas", service.listar());
        model.addAttribute("paginaActiva", "peliculas");
        return "peliculas/lista";
    }
}