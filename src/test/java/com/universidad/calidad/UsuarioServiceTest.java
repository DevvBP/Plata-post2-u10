package com.universidad.calidad;

import com.universidad.calidad.model.Usuario;
import com.universidad.calidad.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService();
    }

    // ---------- obtenerTodos ----------

    @Test
    @DisplayName("Debe retornar lista de usuarios con dos elementos")
    void testObtenerTodosRetornaUsuarios() {
        List<Usuario> resultado = service.obtenerTodos();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Llamadas repetidas no duplican la lista interna")
    void testObtenerTodosNoAcumula() {
        service.obtenerTodos();
        List<Usuario> segunda = service.obtenerTodos();
        assertEquals(2, segunda.size());
    }

    // ---------- clasificarUsuario ----------

    @Test
    @DisplayName("Usuario nulo retorna 'Usuario nulo'")
    void testClasificarNulo() {
        assertEquals("Usuario nulo", service.clasificarUsuario(null));
    }

    @Test
    @DisplayName("Usuario sin rol retorna 'Sin rol asignado'")
    void testClasificarSinRol() {
        Usuario u = new Usuario(1L, "Ana", "ana@uni.edu", null);
        assertEquals("Sin rol asignado", service.clasificarUsuario(u));
    }

    @Test
    @DisplayName("Admin con nombre largo")
    void testAdminNombreLargo() {
        Usuario u = new Usuario(1L, "Carlos Lopez", "carlos@uni.edu", "ADMIN");
        assertEquals("Administrador con nombre largo", service.clasificarUsuario(u));
    }

    @Test
    @DisplayName("Admin con nombre corto")
    void testAdminNombreCorto() {
        Usuario u = new Usuario(1L, "Ana", "ana@uni.edu", "ADMIN");
        assertEquals("Administrador con nombre corto", service.clasificarUsuario(u));
    }

    @Test
    @DisplayName("Admin sin nombre")
    void testAdminSinNombre() {
        Usuario u = new Usuario(1L, null, "admin@uni.edu", "ADMIN");
        assertEquals("Administrador sin nombre", service.clasificarUsuario(u));
    }

    @Test
    @DisplayName("Estudiante universitario")
    void testEstudianteUniversitario() {
        Usuario u = new Usuario(2L, "Luis", "luis@uni.edu", "ESTUDIANTE");
        assertEquals("Estudiante universitario", service.clasificarUsuario(u));
    }

    @Test
    @DisplayName("Estudiante externo")
    void testEstudianteExterno() {
        Usuario u = new Usuario(2L, "Pedro", "pedro@gmail.com", "ESTUDIANTE");
        assertEquals("Estudiante externo", service.clasificarUsuario(u));
    }

    @Test
    @DisplayName("Estudiante sin email")
    void testEstudianteSinEmail() {
        Usuario u = new Usuario(2L, "Juan", null, "ESTUDIANTE");
        assertEquals("Estudiante sin email", service.clasificarUsuario(u));
    }

    @Test
    @DisplayName("Profesor clasificado correctamente")
    void testProfesor() {
        Usuario u = new Usuario(3L, "Docente", "d@uni.edu", "PROFESOR");
        assertEquals("Profesor", service.clasificarUsuario(u));
    }

    @Test
    @DisplayName("Rol desconocido")
    void testRolDesconocido() {
        Usuario u = new Usuario(4L, "X", "x@uni.edu", "OTRO");
        assertEquals("Rol desconocido", service.clasificarUsuario(u));
    }

    // ---------- guardarUsuario ----------

    @Test
    @DisplayName("Guardar usuario valido retorna true")
    void testGuardarValido() {
        Usuario u = new Usuario(5L, "Sofia Rios", "sofia@uni.edu", "ESTUDIANTE");
        assertTrue(service.guardarUsuario(u));
    }

    @Test
    @DisplayName("Guardar usuario nulo retorna false")
    void testGuardarNulo() {
        assertFalse(service.guardarUsuario(null));
    }

    @Test
    @DisplayName("Usuario guardado aparece en la lista")
    void testUsuarioGuardadoApareceLista() {
        service.obtenerTodos();
        Usuario u = new Usuario(10L, "Nuevo", "nuevo@uni.edu", "ESTUDIANTE");
        service.guardarUsuario(u);
        List<Usuario> lista = service.obtenerTodos();
        assertTrue(lista.stream().anyMatch(x -> x.getId().equals(10L)));
    }

    // ---------- buscarPorId ----------

    @Test
    @DisplayName("Buscar usuario existente retorna Optional con valor")
    void testBuscarPorIdExistente() {
        service.obtenerTodos();
        Optional<Usuario> encontrado = service.buscarPorId(1L);
        assertTrue(encontrado.isPresent());
        assertEquals("Carlos Lopez", encontrado.get().getNombre());
    }

    @Test
    @DisplayName("Buscar usuario inexistente retorna Optional vacio")
    void testBuscarPorIdInexistente() {
        service.obtenerTodos();
        Optional<Usuario> encontrado = service.buscarPorId(999L);
        assertTrue(encontrado.isEmpty());
    }

    // ---------- eliminarUsuario ----------

    @Test
    @DisplayName("Eliminar usuario existente retorna true")
    void testEliminarExistente() {
        service.obtenerTodos();
        assertTrue(service.eliminarUsuario(1L));
    }

    @Test
    @DisplayName("Eliminar usuario inexistente retorna false")
    void testEliminarInexistente() {
        service.obtenerTodos();
        assertFalse(service.eliminarUsuario(999L));
    }

    @Test
    @DisplayName("Tras eliminar, el usuario ya no aparece en la lista")
    void testEliminadoNoEstaEnLista() {
        service.obtenerTodos();
        service.eliminarUsuario(1L);
        List<Usuario> lista = service.obtenerTodos();
        assertTrue(lista.stream().noneMatch(u -> u.getId().equals(1L)));
    }

    // ---------- validarPassword ----------

    @Test
    @DisplayName("Passwords iguales retorna true")
    void testPasswordsIguales() {
        assertTrue(service.validarPassword("secreto", "secreto"));
    }

    @Test
    @DisplayName("Passwords distintas retorna false")
    void testPasswordsDistintas() {
        assertFalse(service.validarPassword("abc", "xyz"));
    }

    @Test
    @DisplayName("Input nulo retorna false")
    void testPasswordInputNulo() {
        assertFalse(service.validarPassword(null, "secreto"));
    }

    @Test
    @DisplayName("Stored nulo retorna false")
    void testPasswordStoredNulo() {
        assertFalse(service.validarPassword("secreto", null));
    }
}
