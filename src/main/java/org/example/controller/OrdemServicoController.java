package org.example.controller;

import org.example.dto.OrdemServicoResponse;
import org.example.model.OrdemServico;
import org.example.model.Usuario; // Importar Usuario
import org.example.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Importar Authentication
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponse>> getAllOrdensServico(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(
            ordemServicoService.findAllByOficina(usuario.getOficina().getId()).stream()
                .map(os -> OrdemServicoResponse.builder()
                    .id(os.getId())
                    .numero(os.getNumero())
                    .status(os.getStatus().name())
                    .dataAbertura(os.getDataAbertura())
                    .dataConclusao(os.getDataConclusao())
                    .valorTotal(os.getValorTotal())
                    .veiculoId(os.getVeiculo().getId())
                    .veiculoPlaca(os.getVeiculo().getPlaca())
                    .build())
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServico> getOrdemServicoById(@PathVariable UUID id) {
        return ordemServicoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrdemServico> createOrdemServico(@RequestBody OrdemServico ordemServico) {
        OrdemServico savedOrdemServico = ordemServicoService.save(ordemServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrdemServico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemServico> updateOrdemServico(@PathVariable UUID id, @RequestBody OrdemServico ordemServico) {
        try {
            OrdemServico updatedOrdemServico = ordemServicoService.update(id, ordemServico);
            return ResponseEntity.ok(updatedOrdemServico);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrdemServico(@PathVariable UUID id) {
        if (!ordemServicoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ordemServicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}