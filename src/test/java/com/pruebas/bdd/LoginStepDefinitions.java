package com.pruebas.bdd;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.pruebas.model.Usuario;
import com.pruebas.model.services.UsuarioService;
import com.pruebas.repository.UsuarioRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginStepDefinitions {

    private UsuarioRepository repository;
    private UsuarioService service;
    private Usuario usuarioAutenticado;
    private String correoIngresado;
    private String passwordIngresada;
    private RuntimeException error;

    @Before
    public void prepararEscenario() {
        repository = mock(UsuarioRepository.class);
        service = new UsuarioService();
        // Se inyecta el mock en el atributo privado del servicio para probarlo sin BD.
        try {
            java.lang.reflect.Field campo = UsuarioService.class.getDeclaredField("repository");
            campo.setAccessible(true);
            campo.set(service, repository);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("No se pudo preparar el repositorio simulado", ex);
        }
        usuarioAutenticado = null;
        error = null;
    }

    @Given("existe un usuario registrado con correo {string} y contraseña {string}")
    public void existeUnUsuarioRegistrado(String correo, String password) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombres("Ana");
        usuario.setApellidos("Perez");
        usuario.setEmail(correo);
        usuario.setPassword(password);
        when(repository.findByEmail(correo)).thenReturn(Optional.of(usuario));
    }

    @When("ingreso el correo {string} y la contraseña {string}")
    public void ingresoCredenciales(String correo, String password) {
        correoIngresado = correo;
        passwordIngresada = password;
        try {
            usuarioAutenticado = service.login(correo, password);
        } catch (RuntimeException ex) {
            error = ex;
        }
    }

    @Then("el inicio de sesión es exitoso y se autentica el correo {string}")
    public void loginExitoso(String correoEsperado) {
        assertNull(error);
        assertNotNull(usuarioAutenticado);
        assertEquals(correoEsperado, usuarioAutenticado.getEmail());
        assertEquals(correoIngresado, usuarioAutenticado.getEmail());
        assertEquals("Clave123", passwordIngresada);
    }

    @Then("el inicio de sesión falla con el mensaje {string}")
    public void loginFallido(String mensajeEsperado) {
        assertNull(usuarioAutenticado);
        assertNotNull(error);
        assertEquals(mensajeEsperado, error.getMessage());
    }
}
