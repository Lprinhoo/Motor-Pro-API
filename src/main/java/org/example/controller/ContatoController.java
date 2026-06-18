package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.ContatoRequest;
import org.example.dto.ContatoResponse;
import org.example.model.Contato;
import org.example.service.ContatoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/oficinas/{oficinaId}/contatos")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    private ContatoResponse toResponse(Contato contato) {
        return new ContatoResponse(contato.getId(), contato.getTipo(), contato.getValor());
    }

    @GetMapping
    public ResponseEntity<List<ContatoResponse>> listar(@PathVariable UUID oficinaId) {
        List<ContatoResponse> contatos = contatoService.listar(oficinaId)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(contatos);
    }

    @PostMapping
    public ResponseEntity<ContatoResponse> adicionar(@PathVariable UUID oficinaId,
                                                     @Valid @RequestBody ContatoRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        Contato contato = contatoService.adicionar(oficinaId, request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(contato));
    }

    @PutMapping("/{contatoId}")
    public ResponseEntity<ContatoResponse> atualizar(@PathVariable UUID oficinaId,
                                                     @PathVariable UUID contatoId,
                                                     @Valid @RequestBody ContatoRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        Contato contato = contatoService.atualizar(oficinaId, contatoId, request, userDetails.getUsername());
        return ResponseEntity.ok(toResponse(contato));
    }

    @DeleteMapping("/{contatoId}")
    public ResponseEntity<Void> deletar(@PathVariable UUID oficinaId,
                                        @PathVariable UUID contatoId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        contatoService.deletar(oficinaId, contatoId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
