package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.service.SerieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final SerieService serieService;

    public UserController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("seriesDestacadas", serieService.listarRecientes());
        return "usuario/Home";
    }

}
