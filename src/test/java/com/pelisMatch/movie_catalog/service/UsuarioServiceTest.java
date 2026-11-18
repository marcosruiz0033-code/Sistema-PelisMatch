package com.pelisMatch.movie_catalog.service;

import com.pelisMatch.movie_catalog.model.Usuario;
import com.pelisMatch.movie_catalog.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario(String correo) {
        Usuario u = new Usuario();
        u.setNombre("Frank");
        u.setCorreo(correo);
        u.setRol("USER");
        return u;
    }

    @Test
    void buscarPorCorreo_encuentraUsuarioExistente() {
        when(repository.findByCorreoIgnoreCase("frank@utp.edu.pe"))
                .thenReturn(Optional.of(usuario("frank@utp.edu.pe")));

        assertThat(usuarioService.buscarPorCorreo("frank@utp.edu.pe")).isPresent();
    }

    @Test
    void buscarPorCorreo_devuelveVacioSiNoExiste() {
        when(repository.findByCorreoIgnoreCase("noexiste@utp.edu.pe"))
                .thenReturn(Optional.empty());

        assertThat(usuarioService.buscarPorCorreo("noexiste@utp.edu.pe")).isEmpty();
    }

    @Test
    void guardar_persisteElUsuario() {
        Usuario nuevo = usuario("nuevo@utp.edu.pe");
        when(repository.save(nuevo)).thenReturn(nuevo);

        assertThat(usuarioService.guardar(nuevo)).isSameAs(nuevo);
        verify(repository).save(nuevo);
    }

    @Test
    void contar_cuentaSoloUsuariosConRolUser() {
        when(repository.countByRolIgnoreCase("USER")).thenReturn(3L);

        assertThat(usuarioService.contar()).isEqualTo(3);
        verify(repository).countByRolIgnoreCase("USER");
    }

    @Test
    void listar_devuelveTodosLosUsuarios() {
        when(repository.findAll())
                .thenReturn(List.of(usuario("a@utp.edu.pe"), usuario("b@utp.edu.pe")));

        assertThat(usuarioService.listar()).hasSize(2);
    }

}
