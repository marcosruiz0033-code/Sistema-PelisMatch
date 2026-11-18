package com.pelisMatch.movie_catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @NotBlank
    @Size(min=3, max = 30)
    @Column(nullable = false,
            length = 60)
    private String nombre;

    @NotBlank
    @Email(message = "Ingresa un correo electrónico válido")
    @Column(nullable = false,
            unique = true,
            length =  100)
    private String correo;

    @NotBlank
    @Size(min=3, max = 100)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol;

}
