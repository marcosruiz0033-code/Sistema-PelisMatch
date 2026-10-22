package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/peliculas")
public class AdminPeliculaViewController {

    private final PeliculaService service;

    public AdminPeliculaViewController(PeliculaService service) {
        this.service = service;
    }

    // Listar películas del administrador
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("peliculas", service.listar());
        return "Admin-vista/ProductosVista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "peliculas/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("pelicula", service.obtenerPorId(id));
        return "peliculas/form";
    }

    // Guardar película nueva o actualizar una existente
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("pelicula") Pelicula pelicula,
            BindingResult result) {

        if (result.hasErrors()) {
            return "peliculas/form";
        }

        service.guardar(pelicula);
        return "redirect:/admin/peliculas";
    }

    // Eliminar película
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/admin/peliculas";
    }
}