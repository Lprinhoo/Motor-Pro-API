package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.ContatoResponse;
import org.example.dto.OficinaRequest;
import org.example.dto.OficinaResponse;
import org.example.model.Contato;
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

    private ContatoResponse toContatoResponse(Contato contato) {
        return new ContatoResponse(contato.getId(), contato.getTipo(), contato.getValor());
    }

    private OficinaResponse toOficinaResponse(Oficina oficina) {
        if (oficina == null) return null;
        List<ContatoResponse> contatos = oficina.getContatos().stream()
                .map(this::toContatoResponse)
                .collect(Collectors.toList());
        return new OficinaResponse(oficina.getId(), oficina.getNome(), oficina.getEndereco(), contatos);
    }

    @PostMapping
    public ResponseEntity<OficinaResponse> criar(@Valid @RequestBody OficinaRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Oficina oficina = oficinaService.criar(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(toOficinaResponse(oficina));
    }

    @GetMapping
    public ResponseEntity<List<OficinaResponse>> listar() {
        List<OficinaResponse> oficinas = oficinaService.findAll().stream()
                .map(this::toOficinaResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(oficinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable UUID id) {
        return oficinaService.findById(id)
                .map(this::toOficinaResponse)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oficina não encontrada."));
    }

    @GetMapping("/minha")
    public ResponseEntity<?> buscarMinha(@AuthenticationPrincipal UserDetails userDetails) {
        return oficinaService.findByUsername(userDetails.getUsername())
                .map(this::toOficinaResponse)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma oficina cadastrada."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OficinaResponse> atualizar(@PathVariable UUID id,
                                                     @Valid @RequestBody OficinaRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        Oficina oficina = oficinaService.update(id, request, userDetails.getUsername());
        return ResponseEntity.ok(toOficinaResponse(oficina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        oficinaService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
