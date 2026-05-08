package com.universidad.calidad.controller;

import com.universidad.calidad.model.Usuario;
import com.universidad.calidad.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        boolean guardado = usuarioService.guardarUsuario(usuario);
        if (guardado) {
            return ResponseEntity.ok("Usuario guardado exitosamente");
        }
        return ResponseEntity.badRequest().body("Error al guardar el usuario");
    }

    @GetMapping("/clasificar/{rol}")
    public ResponseEntity<String> clasificar(@PathVariable String rol) {
        Usuario u = new Usuario(99L, "Test", "test@uni.edu", rol);
        String clasificacion = usuarioService.clasificarUsuario(u);
        return ResponseEntity.ok(clasificacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        boolean eliminado = usuarioService.eliminarUsuario(id);
        if (eliminado) {
            return ResponseEntity.ok("Usuario eliminado");
        }
        return ResponseEntity.notFound().build();
    }
}
