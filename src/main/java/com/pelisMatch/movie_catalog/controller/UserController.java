package com.pelisMatch.movie_catalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String home() {
        return "usuario/Home";
    }

    @GetMapping("/series")
    public String series() {
        return "usuario/Series";
    }

}
