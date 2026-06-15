package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.ServicoRequest;
import org.example.dto.ServicoResponse; // Import adicionado
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
    public ResponseEntity<ServicoResponse> createServico(@Valid @RequestBody ServicoRequest request) { // Tipo de retorno alterado
        String username = getAuthenticatedUsername();
        ServicoResponse servicoResponse = servicoService.createServico(request, username); // Tipo alterado
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoResponse);
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> getServicosByOficina() { // Tipo de retorno alterado
        String username = getAuthenticatedUsername();
        List<ServicoResponse> servicos = servicoService.getServicosByOficina(username); // Tipo alterado
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> getServicoById(@PathVariable UUID id) { // Tipo de retorno alterado
        String username = getAuthenticatedUsername();
        return servicoService.getServicoById(id, username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> updateServico(@PathVariable UUID id, @Valid @RequestBody ServicoRequest request) { // Tipo de retorno alterado
        String username = getAuthenticatedUsername();
        ServicoResponse updatedServico = servicoService.updateServico(id, request, username); // Tipo alterado
        return ResponseEntity.ok(updatedServico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServico(@PathVariable UUID id) {
        String username = getAuthenticatedUsername();
        servicoService.deleteServico(id, username);
        return ResponseEntity.noContent().build();
    }
}