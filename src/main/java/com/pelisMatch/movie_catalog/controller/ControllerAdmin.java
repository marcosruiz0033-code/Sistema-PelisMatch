package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import com.pelisMatch.movie_catalog.service.PeliculaService;
import com.pelisMatch.movie_catalog.service.SerieService;
import com.pelisMatch.movie_catalog.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ControllerAdmin {

    private final PeliculaService peliculaService;
    private final SerieService serieService;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public ControllerAdmin(PeliculaService peliculaService, SerieService serieService,
                           UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.peliculaService = peliculaService;
        this.serieService = serieService;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String Dashboard(Model model, HttpSession session) {
        model.addAttribute("totalPeliculas", peliculaService.contar());
        model.addAttribute("totalSeries", serieService.contar());
        model.addAttribute("totalUsuarios", usuarioService.contar());
        model.addAttribute("nombreAdministrador", session.getAttribute("usuarioNombre"));
        return "Admin-vista/Dashboard";
    }

    @GetMapping("/productos")
    public String productos() {
        return "redirect:/admin/peliculas";
    }

    @GetMapping("/pedidos")
    public String pedidos() {
        return "redirect:/admin/series";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("administrador", new Usuario());
        return "Admin-vista/UsuariosVista";
    }

    @PostMapping("/usuarios/administrador")
    public String crearAdministrador(@Valid @ModelAttribute("administrador") Usuario administrador,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        administrador.setRol("ADMIN");
        String correo = administrador.getCorreo() == null ? "" : administrador.getCorreo().trim();
        if (usuarioService.buscarPorCorreo(correo).isPresent()) {
            result.rejectValue("correo", "duplicado", "Este correo ya está registrado.");
        }
        if (result.hasErrors()) {
            model.addAttribute("usuarios", usuarioService.listar());
            return "Admin-vista/UsuariosVista";
        }

        administrador.setCorreo(correo.toLowerCase());
        administrador.setPassword(passwordEncoder.encode(administrador.getPassword()));
        usuarioService.guardar(administrador);
        redirectAttributes.addFlashAttribute("mensajeExito", "Administrador creado correctamente.");
        return "redirect:/admin/usuarios";
    }
}
