package com.pelisMatch.movie_catalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ControllerAdmin {

    @GetMapping("/dashboard")
    public String Dashboard (){
        return "Admin-vista/Dashboard";
    }
    @GetMapping("/productos")
    public String productos (){
        return "Admin-vista/ProductosVista";
    }
    @GetMapping("/pedidos")
    public String pedidos (){
        return "Admin-vista/PedidosVista";
    }
    @GetMapping("/usuarios")
    public String usuarios (){
        return "Admin-vista/UsuariosVista";
    }

}
