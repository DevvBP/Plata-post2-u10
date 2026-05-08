package com.universidad.calidad.service;

import com.universidad.calidad.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final List<Usuario> usuarios = new ArrayList<>();

    public List<Usuario> obtenerTodos() {
        if (usuarios.isEmpty()) {
            usuarios.add(new Usuario(1L, "Carlos Lopez", "carlos@uni.edu", "ADMIN"));
            usuarios.add(new Usuario(2L, "Maria Garcia", "maria@uni.edu", "ESTUDIANTE"));
        }
        return new ArrayList<>(usuarios);
    }

    public String clasificarUsuario(Usuario usuario) {
        if (usuario == null) {
            return "Usuario nulo";
        }
        if (usuario.getRol() == null) {
            return "Sin rol asignado";
        }
        return switch (usuario.getRol()) {
            case "ADMIN" -> clasificarAdmin(usuario);
            case "ESTUDIANTE" -> clasificarEstudiante(usuario);
            case "PROFESOR" -> "Profesor";
            default -> "Rol desconocido";
        };
    }

    private String clasificarAdmin(Usuario usuario) {
        if (usuario.getNombre() == null) {
            return "Administrador sin nombre";
        }
        return usuario.getNombre().length() > 5
                ? "Administrador con nombre largo"
                : "Administrador con nombre corto";
    }

    private String clasificarEstudiante(Usuario usuario) {
        if (usuario.getEmail() == null) {
            return "Estudiante sin email";
        }
        return usuario.getEmail().contains("@uni.edu")
                ? "Estudiante universitario"
                : "Estudiante externo";
    }

    public boolean guardarUsuario(Usuario usuario) {
        try {
            if (usuario == null) {
                throw new IllegalArgumentException("El usuario no puede ser nulo");
            }
            usuarios.add(usuario);
            return true;
        } catch (IllegalArgumentException e) {
            log.warn("Intento de guardar usuario invalido: {}", e.getMessage());
            return false;
        }
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarios.stream()
                .filter(u -> u.getId() != null && u.getId().equals(id))
                .findFirst();
    }

    public boolean eliminarUsuario(Long id) {
        Optional<Usuario> encontrado = buscarPorId(id);
        if (encontrado.isEmpty()) {
            log.info("No se encontro usuario con id: {}", id);
            return false;
        }
        usuarios.remove(encontrado.get());
        log.info("Usuario con id {} eliminado correctamente", id);
        return true;
    }

    public boolean validarPassword(String input, String stored) {
        if (input == null || stored == null) {
            return false;
        }
        return input.equals(stored);
    }
}
