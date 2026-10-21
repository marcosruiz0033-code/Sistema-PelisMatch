package com.pelisMatch.movie_catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Size(min=3, max = 30)
    @Column(nullable = false,
            length = 60)
    private String nombre;

    @NotBlank
    @Column(nullable = false,
            unique = true,
            length =  100)
    private String correo;

    @NotBlank
    @Size(min=3, max = 100)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String rol;

}
