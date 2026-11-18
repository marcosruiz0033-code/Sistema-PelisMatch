package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.service.FavoritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @GetMapping("/favoritos")
    public String favoritos(HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        model.addAttribute("peliculasFavoritas", favoritoService.peliculasFavoritas(usuarioId));
        model.addAttribute("seriesFavoritas", favoritoService.seriesFavoritas(usuarioId));
        model.addAttribute("paginaActiva", "favoritos");
        return "usuario/Favoritos";
    }

    @GetMapping("/api/favoritos")
    @ResponseBody
    public Map<String, ?> apiFavoritos(HttpSession session) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        return favoritoService.idsFavoritos(usuarioId);
    }

    @PostMapping("/api/favoritos/alternar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> alternar(@RequestParam String tipo,
                                                        @RequestParam long id,
                                                        HttpSession session) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        boolean agregado;
        switch (tipo) {
            case "pelicula" -> agregado = favoritoService.alternarPelicula(usuarioId, id);
            case "serie" -> agregado = favoritoService.alternarSerie(usuarioId, (int) id);
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(Map.of("favorito", agregado));
    }

}
