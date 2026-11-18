package com.pelisMatch.movie_catalog.controller;

import com.pelisMatch.movie_catalog.model.Usuario;
import com.pelisMatch.movie_catalog.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("usuarioRol") != null) {
            return inicioPorRol(session);
        }
        return "auth/Login";
    }

    @PostMapping("/login")
    public String ingresar(String correo, String password, HttpSession session, Model model) {
        Usuario usuario = usuarioService.buscarPorCorreo(correo == null ? "" : correo.trim())
                .orElse(null);
        if (usuario == null || !passwordEncoder.matches(password == null ? "" : password, usuario.getPassword())) {
            model.addAttribute("error", "El correo o la contraseña no son correctos.");
            return "auth/Login";
        }

        session.setAttribute("usuarioId", usuario.getIdUsuario());
        session.setAttribute("usuarioNombre", usuario.getNombre());
        session.setAttribute("usuarioCorreo", usuario.getCorreo());
        session.setAttribute("usuarioRol", usuario.getRol());
        return inicioPorRol(session);
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/Registro";
    }

    @PostMapping("/registro")
    public String crearCuenta(@Valid @ModelAttribute("usuario") Usuario usuario,
                              BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        usuario.setRol("USER");
        String correo = usuario.getCorreo() == null ? "" : usuario.getCorreo().trim();
        if (usuarioService.buscarPorCorreo(correo).isPresent()) {
            result.rejectValue("correo", "duplicado", "Este correo ya está registrado.");
        }
        if (result.hasErrors()) {
            return "auth/Registro";
        }

        usuario.setCorreo(correo.toLowerCase());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioService.guardar(usuario);
        redirectAttributes.addFlashAttribute("registroExitoso", "Tu cuenta fue creada. Ya puedes iniciar sesión.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String salir(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String inicioPorRol(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("usuarioRol"))
                ? "redirect:/admin/dashboard"
                : "redirect:/";
    }
}
