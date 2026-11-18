package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.service.SerieService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/series")
public class SerieController {

    private static final int TAMANIO_PAGINA = 14;

    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<Serie> page = serieService.listarPagina(pagina, TAMANIO_PAGINA);
        model.addAttribute("series", page.getContent());
        model.addAttribute("paginaActual", page.getNumber());
        model.addAttribute("totalPaginas", page.getTotalPages());
        model.addAttribute("paginaActiva", "series");
        return "usuario/Series";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        return serieService.buscarPorId(id)
                .map(serie -> {
                    model.addAttribute("serie", serie);
                    model.addAttribute("paginaActiva", "series");
                    return "usuario/SerieDetalle";
                })
                .orElse("redirect:/series");
    }

}
