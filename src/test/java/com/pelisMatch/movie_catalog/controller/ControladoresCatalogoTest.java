package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Pelicula;
import com.pelisMatch.movie_catalog.model.Serie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración de las pantallas principales del módulo usuario.
 * Corren sobre H2 (perfil "test"), sin necesidad de Postgres.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ControladoresCatalogoTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession sesionUsuario() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioRol", "USER");
        session.setAttribute("usuarioId", 1);
        session.setAttribute("usuarioNombre", "Frank");
        return session;
    }

    private MockHttpSession sesionAdministrador() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("usuarioRol", "ADMIN");
        session.setAttribute("usuarioId", 1);
        session.setAttribute("usuarioNombre", "Administrador");
        return session;
    }

    // ===== Autenticación =====

    @Test
    void login_muestraLaVista() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/Login"));
    }

    @Test
    void sinSesion_cualquierPaginaRedirigeALogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(get("/series"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(get("/peliculas"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(get("/favoritos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // ===== Inicio =====

    @Test
    void home_conSesionUsuario_muestraElInicio() throws Exception {
        mockMvc.perform(get("/").session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario/Home"))
                .andExpect(model().attributeExists("seriesDestacadas", "peliculasRecientes", "paginaActiva"));
    }

    // ===== Series + paginación =====

    @Test
    void series_conSesion_muestraListadoYPaginacion() throws Exception {
        mockMvc.perform(get("/series").session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario/Series"))
                .andExpect(model().attributeExists("series", "paginaActual", "totalPaginas"))
                .andExpect(model().attribute("paginaActiva", "series"));
    }

    @Test
    void series_porParametroDePagina_respondeOk() throws Exception {
        mockMvc.perform(get("/series").param("pagina", "1").session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("paginaActual", 1));
    }

    @Test
    void series_paginaFueraDeRango_noFallaYUsaUnaPaginaValida() throws Exception {
        mockMvc.perform(get("/series").param("pagina", "999").session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("series"));
    }

    @Test
    void series_filtraCategoriaYConservaElOrdenSeleccionado() throws Exception {
        var resultado = mockMvc.perform(get("/series")
                        .param("genero", "Ciencia ficción")
                        .param("orden", "az")
                        .session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("generoActual", "Ciencia ficción"))
                .andExpect(model().attribute("ordenActual", "az"))
                .andReturn();

        List<?> series = (List<?>) resultado.getModelAndView().getModel().get("series");
        assertThat(series).isNotEmpty().allSatisfy(contenido ->
                assertThat(((Serie) contenido).getCategoria().getNombre())
                        .isEqualTo("Ciencia ficción"));
    }

    @Test
    void serieInexistente_redirigeAlListado() throws Exception {
        mockMvc.perform(get("/series/999999").session(sesionUsuario()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/series"));
    }

    @Test
    void serieExistente_muestraElDetalle() throws Exception {
        mockMvc.perform(get("/series/1").session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario/SerieDetalle"))
                .andExpect(model().attributeExists("serie"));
    }

    // ===== Películas + paginación =====

    @Test
    void peliculas_conSesion_muestraListadoYPaginacion() throws Exception {
        mockMvc.perform(get("/peliculas").session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(view().name("peliculas/lista"))
                .andExpect(model().attributeExists("peliculas", "paginaActual", "totalPaginas"))
                .andExpect(model().attribute("paginaActiva", "peliculas"));
    }

    @Test
    void peliculas_porParametroDePagina_respondeOk() throws Exception {
        mockMvc.perform(get("/peliculas").param("pagina", "1").session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("paginaActual", 1));
    }

    @Test
    void peliculas_filtraPorTextoYCategoria() throws Exception {
        var resultado = mockMvc.perform(get("/peliculas")
                        .param("q", "Matrix")
                        .param("genero", "Ciencia ficción")
                        .param("orden", "az")
                        .session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("q", "Matrix"))
                .andExpect(model().attribute("generoActual", "Ciencia ficción"))
                .andExpect(model().attribute("ordenActual", "az"))
                .andReturn();

        List<?> peliculas = (List<?>) resultado.getModelAndView().getModel().get("peliculas");
        assertThat(peliculas).isNotEmpty().allSatisfy(contenido -> {
            Pelicula pelicula = (Pelicula) contenido;
            String categoria = pelicula.getCategoria() == null
                    ? pelicula.getGenero()
                    : pelicula.getCategoria().getNombre();
            assertThat(categoria).isEqualToIgnoringCase("Ciencia ficción");
        });
    }

    @Test
    void peliculaInexistente_redirigeAlListado() throws Exception {
        mockMvc.perform(get("/peliculas/999999").session(sesionUsuario()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peliculas"));
    }

    // ===== Búsqueda =====

    @Test
    void buscar_conConsulta_muestraResultados() throws Exception {
        mockMvc.perform(get("/buscar").param("q", "a").session(sesionUsuario()))
                .andExpect(status().isOk())
                .andExpect(view().name("usuario/Search"))
                .andExpect(model().attributeExists("peliculas", "series", "consulta"));
    }

    @Test
    void dashboardAdmin_muestraActividadReciente() throws Exception {
        mockMvc.perform(get("/admin/dashboard").session(sesionAdministrador()))
                .andExpect(status().isOk())
                .andExpect(view().name("Admin-vista/Dashboard"))
                .andExpect(model().attributeExists(
                        "ultimasPeliculas", "ultimasSeries", "totalPeliculas"));
    }

    @Test
    void formularioPelicula_muestraLasCategoriasLegibles() throws Exception {
        String html = mockMvc.perform(get("/admin/peliculas/nueva")
                        .session(sesionAdministrador()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains(">Acción</option>", ">Ciencia ficción</option>", ">Animación</option>");
    }

}
