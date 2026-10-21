package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.service.SerieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        return serieService.buscarPorId(id)
                .map(serie -> {
                    model.addAttribute("serie", serie);
                    return "usuario/SerieDetalle";
                })
                .orElse("redirect:/series");
    }

}
