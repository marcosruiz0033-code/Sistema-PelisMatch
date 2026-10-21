package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.service.SerieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final SerieService serieService;

    public UserController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Serie> destacadas = serieService.listarDestacadas();
        model.addAttribute("seriesDestacadas", destacadas);
        model.addAttribute("seriesAdicionales", serieService.listarAdicionales(destacadas));
        return "usuario/Home";
    }

}
