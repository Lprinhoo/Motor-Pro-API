package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.ServicoRequest;
import org.example.dto.ServicoResponse; // Import adicionado
import org.example.model.Servico;
import org.example.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // Import adicionado

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
    public ResponseEntity<ServicoResponse> createServico(@Valid @RequestBody ServicoRequest request) {
        String username = getAuthenticatedUsername();
        Servico servico = servicoService.createServico(request, username);
        // Converte Servico para ServicoResponse
        ServicoResponse servicoResponse = new ServicoResponse(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoResponse);
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> getServicosByOficina() {
        String username = getAuthenticatedUsername();
        List<Servico> servicos = servicoService.getServicosByOficina(username);
        // Converte List<Servico> para List<ServicoResponse>
        List<ServicoResponse> servicoResponses = servicos.stream()
                .map(servico -> new ServicoResponse(
                        servico.getId(),
                        servico.getNome(),
                        servico.getDescricao(),
                        servico.getPreco()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(servicoResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> getServicoById(@PathVariable UUID id) {
        String username = getAuthenticatedUsername();
        return servicoService.getServicoById(id, username)
                .map(servico -> new ServicoResponse( // Converte Servico para ServicoResponse
                        servico.getId(),
                        servico.getNome(),
                        servico.getDescricao(),
                        servico.getPreco()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> updateServico(@PathVariable UUID id, @Valid @RequestBody ServicoRequest request) {
        String username = getAuthenticatedUsername();
        Servico updatedServico = servicoService.updateServico(id, request, username);
        // Converte Servico para ServicoResponse
        ServicoResponse servicoResponse = new ServicoResponse(
                updatedServico.getId(),
                updatedServico.getNome(),
                updatedServico.getDescricao(),
                updatedServico.getPreco()
        );
        return ResponseEntity.ok(servicoResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServico(@PathVariable UUID id) {
        String username = getAuthenticatedUsername();
        servicoService.deleteServico(id, username);
        return ResponseEntity.noContent().build();
    }
}