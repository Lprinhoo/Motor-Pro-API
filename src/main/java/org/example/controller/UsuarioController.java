package org.example.controller;

import org.example.dto.UsuarioResponse;
import org.example.model.Usuario; // Importar Usuario
import org.example.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Importar Authentication
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(
            usuarioService.findAllByOficina(usuario.getOficina().getId()).stream()
                .map(u -> UsuarioResponse.builder()
                    .id(u.getId())
                    .nome(u.getNome())
                    .email(u.getEmail())
                    .role(u.getRole())
                    .oficinaId(u.getOficina() != null ? u.getOficina().getId() : null)
                    .oficinaNome(u.getOficina() != null ? u.getOficina().getNome() : null)
                    .build())
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable UUID id) {
        return usuarioService.findById(id)
                .map(usuario -> ResponseEntity.ok(UsuarioResponse.builder()
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .role(usuario.getRole())
                    .oficinaId(usuario.getOficina() != null ? usuario.getOficina().getId() : null)
                    .oficinaNome(usuario.getOficina() != null ? usuario.getOficina().getNome() : null)
                    .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        Usuario savedUsuario = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable UUID id, @RequestBody Usuario usuario) {
        try {
            Usuario updatedUsuario = usuarioService.update(id, usuario);
            return ResponseEntity.ok(updatedUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID id) {
        if (!usuarioService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}