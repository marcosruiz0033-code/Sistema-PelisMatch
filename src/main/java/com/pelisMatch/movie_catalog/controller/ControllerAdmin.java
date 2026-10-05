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
}
