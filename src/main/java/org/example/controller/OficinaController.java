package org.example.controller;

import org.example.dto.OficinaRequest;
import org.example.dto.OficinaResponse; // Importar o DTO de resposta
import org.example.model.Oficina;
import org.example.service.OficinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaController {

    private final OficinaService oficinaService;

    public OficinaController(OficinaService oficinaService) {
        this.oficinaService = oficinaService;
    }

    // Método auxiliar para converter Oficina para OficinaResponse
    private OficinaResponse toOficinaResponse(Oficina oficina) {
        if (oficina == null) {
            return null;
        }
        return new OficinaResponse(
                oficina.getId(),
                oficina.getNome(),
                oficina.getEndereco(),
                oficina.getTelefone(),
                oficina.getEmail()
        );
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody OficinaRequest request,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Oficina oficina = oficinaService.criar(request, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(toOficinaResponse(oficina)); // Retorna DTO
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<OficinaResponse>> listar() { // Retorna lista de DTOs
        List<OficinaResponse> oficinas = oficinaService.findAll().stream()
                .map(this::toOficinaResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(oficinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable UUID id) {
        return oficinaService.findById(id)
                .map(this::toOficinaResponse) // Mapeia para DTO
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oficina não encontrada."));
    }

    // Endpoint para buscar a oficina do usuário autenticado
    @GetMapping("/minha")
    public ResponseEntity<?> buscarMinha(@AuthenticationPrincipal UserDetails userDetails) {
        return oficinaService.findByUsername(userDetails.getUsername())
                .map(this::toOficinaResponse) // Mapeia para DTO
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma oficina cadastrada."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable UUID id,
                                       @RequestBody OficinaRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Oficina oficina = oficinaService.update(id, request, userDetails.getUsername());
            return ResponseEntity.ok(toOficinaResponse(oficina)); // Retorna DTO
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable UUID id,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        try {
            oficinaService.delete(id, userDetails.getUsername());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}