package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    private final PeliculaService service;

    public PeliculaController(PeliculaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pelicula> listar(@RequestParam(required = false) String genero) {
        return genero == null ? service.listar() : service.listarPorGenero(genero);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> obtener(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pelicula crear(@Valid @RequestBody Pelicula pelicula) {
        return service.guardar(pelicula);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> actualizar(@PathVariable Long id, @Valid @RequestBody Pelicula pelicula) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pelicula.setId(id);
        return ResponseEntity.ok(service.guardar(pelicula));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
