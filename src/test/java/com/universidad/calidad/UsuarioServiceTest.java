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

    @Test
    @DisplayName("La lista retornada es una copia independiente")
    void testObtenerTodosRetornaCopia() {
        List<Usuario> primera = service.obtenerTodos();
        primera.clear();
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
    @DisplayName("Admin con nombre de exactamente 5 caracteres es nombre corto")
    void testAdminNombreExacto5() {
        Usuario u = new Usuario(1L, "Pedro", "pedro@uni.edu", "ADMIN");
        assertEquals("Administrador con nombre corto", service.clasificarUsuario(u));
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

    @Test
    @DisplayName("Rol vacio retorna 'Rol desconocido'")
    void testRolVacio() {
        Usuario u = new Usuario(4L, "Test", "test@uni.edu", "");
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

    @Test
    @DisplayName("Guardar multiple usuarios incrementa la lista")
    void testGuardarMultiples() {
        service.obtenerTodos();
        service.guardarUsuario(new Usuario(20L, "A", "a@uni.edu", "ADMIN"));
        service.guardarUsuario(new Usuario(21L, "B", "b@uni.edu", "PROFESOR"));
        List<Usuario> lista = service.obtenerTodos();
        assertEquals(4, lista.size());
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

    @Test
    @DisplayName("Buscar en lista vacia retorna Optional vacio")
    void testBuscarEnListaVacia() {
        Optional<Usuario> encontrado = service.buscarPorId(1L);
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

    @Test
    @DisplayName("Eliminar ambos usuarios iniciales los remueve correctamente")
    void testEliminarTodos() {
        service.obtenerTodos();
        boolean r1 = service.eliminarUsuario(1L);
        boolean r2 = service.eliminarUsuario(2L);
        assertTrue(r1);
        assertTrue(r2);
        assertTrue(service.buscarPorId(1L).isEmpty());
        assertTrue(service.buscarPorId(2L).isEmpty());
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

    @Test
    @DisplayName("Ambos nulos retorna false")
    void testPasswordAmbosNulos() {
        assertFalse(service.validarPassword(null, null));
    }

    @Test
    @DisplayName("Password vacia no coincide con stored")
    void testPasswordVacia() {
        assertFalse(service.validarPassword("", "secreto"));
    }

    // ---------- Usuario model ----------

    @Test
    @DisplayName("Equals con mismo id retorna true")
    void testUsuarioEquals() {
        Usuario a = new Usuario(1L, "Carlos", "c@uni.edu", "ADMIN");
        Usuario b = new Usuario(1L, "Otro", "o@uni.edu", "PROF");
        assertEquals(a, b);
    }

    @Test
    @DisplayName("Equals con distinto id retorna false")
    void testUsuarioNotEquals() {
        Usuario a = new Usuario(1L, "Carlos", "c@uni.edu", "ADMIN");
        Usuario b = new Usuario(2L, "Carlos", "c@uni.edu", "ADMIN");
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("toString contiene datos del usuario")
    void testUsuarioToString() {
        Usuario u = new Usuario(1L, "Carlos", "c@uni.edu", "ADMIN");
        String str = u.toString();
        assertTrue(str.contains("Carlos"));
        assertTrue(str.contains("ADMIN"));
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void testUsuarioSettersGetters() {
        Usuario u = new Usuario();
        u.setId(5L);
        u.setNombre("Test");
        u.setEmail("t@uni.edu");
        u.setRol("ESTUDIANTE");
        assertEquals(5L, u.getId());
        assertEquals("Test", u.getNombre());
        assertEquals("t@uni.edu", u.getEmail());
        assertEquals("ESTUDIANTE", u.getRol());
    }

    @Test
    @DisplayName("HashCode es consistente para mismo id")
    void testHashCode() {
        Usuario a = new Usuario(1L, "A", "a@uni.edu", "ADMIN");
        Usuario b = new Usuario(1L, "B", "b@uni.edu", "PROF");
        assertEquals(a.hashCode(), b.hashCode());
    }
}
