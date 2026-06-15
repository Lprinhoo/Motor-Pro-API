package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.ServicoRequest;
import org.example.model.Servico;
import org.example.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping
    public ResponseEntity<Servico> createServico(@Valid @RequestBody ServicoRequest request) {
        String username = getAuthenticatedUsername();
        Servico servico = servicoService.createServico(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(servico);
    }

    @GetMapping
    public ResponseEntity<List<Servico>> getServicosByOficina() {
        String username = getAuthenticatedUsername();
        List<Servico> servicos = servicoService.getServicosByOficina(username);
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> getServicoById(@PathVariable UUID id) {
        String username = getAuthenticatedUsername();
        return servicoService.getServicoById(id, username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> updateServico(@PathVariable UUID id, @Valid @RequestBody ServicoRequest request) {
        String username = getAuthenticatedUsername();
        Servico updatedServico = servicoService.updateServico(id, request, username);
        return ResponseEntity.ok(updatedServico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServico(@PathVariable UUID id) {
        String username = getAuthenticatedUsername();
        servicoService.deleteServico(id, username);
        return ResponseEntity.noContent().build();
    }
}
