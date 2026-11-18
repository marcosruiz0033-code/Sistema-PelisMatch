package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Serie;
import com.pelisMatch.movie_catalog.service.CategoriaService;
import com.pelisMatch.movie_catalog.service.SerieService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/series")
public class AdminSerieViewController {

    private final SerieService serieService;
    private final CategoriaService categoriaService;

    public AdminSerieViewController(SerieService serieService, CategoriaService categoriaService) {
        this.serieService = serieService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("series", serieService.listarTodas());
        return "Admin-vista/PedidosVista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("serie", new Serie());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "Admin-vista/SerieForm";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        return serieService.buscarPorId(id).map(serie -> {
            model.addAttribute("serie", serie);
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "Admin-vista/SerieForm";
        }).orElse("redirect:/admin/series");
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("serie") Serie serie,
                          BindingResult result,
                          @RequestParam(required = false) Integer categoriaId,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "Admin-vista/SerieForm";
        }

        serie.setCategoria(categoriaId == null ? null
                : categoriaService.buscarPorId(categoriaId).orElse(null));
        serieService.guardar(serie);
        return "redirect:/admin/series";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        serieService.eliminar(id);
        return "redirect:/admin/series";
    }
}
