package org.example.controller;

import org.example.dto.ServicoResponse;
import org.example.model.Servico;
import org.example.model.Usuario; // Importar Usuario
import org.example.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Importar Authentication
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> getAllServicos(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(
            servicoService.findAllByOficina(usuario.getOficina().getId()).stream()
                .map(servico -> ServicoResponse.builder()
                    .id(servico.getId())
                    .nome(servico.getNome())
                    .descricao(servico.getDescricao())
                    .preco(servico.getPreco())
                    .build())
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> getServicoById(@PathVariable UUID id) {
        return servicoService.findById(id)
                .map(servico -> ResponseEntity.ok(ServicoResponse.builder()
                    .id(servico.getId())
                    .nome(servico.getNome())
                    .descricao(servico.getDescricao())
                    .preco(servico.getPreco())
                    .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Servico> createServico(@RequestBody Servico servico) {
        Servico savedServico = servicoService.save(servico);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedServico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> updateServico(@PathVariable UUID id, @RequestBody Servico servico) {
        try {
            Servico updatedServico = servicoService.update(id, servico);
            return ResponseEntity.ok(updatedServico);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServico(@PathVariable UUID id) {
        if (!servicoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        servicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}