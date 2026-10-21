package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.service.SerieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/series")
public class SerieController {

    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("series", serieService.listarTodas());
        return "usuario/Series";
    }

}
