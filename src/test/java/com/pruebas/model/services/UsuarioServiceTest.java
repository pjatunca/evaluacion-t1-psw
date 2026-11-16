package com.pruebas.model.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.pruebas.model.Usuario;
import com.pruebas.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void registrarUsuarioCorrectamente() {
        Usuario usuario = crearUsuario();
        Usuario guardado = crearUsuario();
        guardado.setId(1L);

        when(repository.findByEmail("ana@correo.com")).thenReturn(Optional.empty());
        when(repository.save(usuario)).thenReturn(guardado);

        Usuario resultado = service.registrar(usuario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("ana@correo.com", resultado.getEmail());
        verify(repository).save(usuario);
    }

    @Test
    void registrarCorreoExistenteLanzaExcepcionYNoGuarda() {
        Usuario usuario = crearUsuario();
        Usuario existente = crearUsuario();
        existente.setId(10L);

        when(repository.findByEmail("ana@correo.com")).thenReturn(Optional.of(existente));

        RuntimeException excepcion = assertThrows(
                RuntimeException.class,
                () -> service.registrar(usuario)
        );

        assertEquals("Correo ya registrado", excepcion.getMessage());
        verify(repository, never()).save(any(Usuario.class));
    }

    private Usuario crearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombres("Ana");
        usuario.setApellidos("Perez");
        usuario.setEmail("ana@correo.com");
        usuario.setPassword("Clave123");
        return usuario;
    }
}
