package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.repository.FavoritoRepository;
import com.pelisMatch.movie_catalog.repository.PeliculaRepository;
import com.pelisMatch.movie_catalog.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración del módulo de favoritos: API JSON + vista.
 * Corren sobre H2 (perfil "test"), sin necesidad de Postgres.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FavoritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    private MockHttpSession session;
    private Long idPelicula;

    @BeforeEach
    void setup() {
        favoritoRepository.deleteAll();
        session = new MockHttpSession();
        session.setAttribute("usuarioRol", "USER");
        session.setAttribute("usuarioNombre", "Frank");
        session.setAttribute("usuarioId", usuarioRepository
                .findByCorreoIgnoreCase("marcos@utp.edu.pe")
                .orElseThrow(() -> new RuntimeException("Falta el usuario sembrado por DataInitializer"))
                .getIdUsuario());
        idPelicula = peliculaRepository.findAll().get(0).getId();
    }

    @Test
    void sinSesion_laVistaYLaApiRedirigenALogin() throws Exception {
        mockMvc.perform(get("/favoritos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(get("/api/favoritos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void api_devuelveListasVaciasAlInicio() throws Exception {
        mockMvc.perform(get("/api/favoritos").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peliculas").isArray())
                .andExpect(jsonPath("$.series").isArray())
                .andExpect(jsonPath("$.peliculas").isEmpty())
                .andExpect(jsonPath("$.series").isEmpty());
    }

    @Test
    void alternarPelicula_agregaQuitaYVuelveAAgregar() throws Exception {
        // Agregar
        mockMvc.perform(post("/api/favoritos/alternar")
                        .param("tipo", "pelicula")
                        .param("id", String.valueOf(idPelicula))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorito").value(true));

        // Aparece en la API
        mockMvc.perform(get("/api/favoritos").session(session))
                .andExpect(jsonPath("$.peliculas").value(org.hamcrest.Matchers.hasItem(idPelicula.intValue())));

        // Quitar
        mockMvc.perform(post("/api/favoritos/alternar")
                        .param("tipo", "pelicula")
                        .param("id", String.valueOf(idPelicula))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorito").value(false));

        // Ya no aparece
        mockMvc.perform(get("/api/favoritos").session(session))
                .andExpect(jsonPath("$.peliculas").isEmpty());
    }

    @Test
    void alternar_conTipoInvalido_respondeBadRequest() throws Exception {
        mockMvc.perform(post("/api/favoritos/alternar")
                        .param("tipo", "libro")
                        .param("id", "1")
                        .session(session))
                .andExpect(status().isBadRequest());
    }

    @Test
    void vista_favoritos_muestraElListado() throws Exception {
        // Marcar uno para que la vista tenga contenido
        mockMvc.perform(post("/api/favoritos/alternar")
                        .param("tipo", "pelicula")
                        .param("id", String.valueOf(idPelicula))
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/favoritos").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario/Favoritos"))
                .andExpect(model().attributeExists("peliculasFavoritas", "seriesFavoritas"))
                .andExpect(model().attribute("paginaActiva", "favoritos"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Mis favoritos")));

        assertThat(favoritoRepository.count()).isEqualTo(1);
    }

}
