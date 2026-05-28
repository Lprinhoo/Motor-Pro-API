package org.example.controller;

import org.example.dto.VeiculoResponse;
import org.example.model.Veiculo;
import org.example.model.Usuario; // Importar Usuario
import org.example.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Importar Authentication
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> getAllVeiculos(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(
            veiculoService.findAllByOficina(usuario.getOficina().getId()).stream()
                .map(veiculo -> VeiculoResponse.builder()
                    .id(veiculo.getId())
                    .placa(veiculo.getPlaca())
                    .marca(veiculo.getMarca())
                    .modelo(veiculo.getModelo()) // Corrigido de getModel() para getModelo()
                    .ano(veiculo.getAno())
                    .clienteId(veiculo.getCliente().getId())
                    .clienteNome(veiculo.getCliente().getNome())
                    .build())
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponse> getVeiculoById(@PathVariable UUID id) {
        return veiculoService.findById(id)
                .map(veiculo -> ResponseEntity.ok(VeiculoResponse.builder()
                    .id(veiculo.getId())
                    .placa(veiculo.getPlaca())
                    .marca(veiculo.getMarca())
                    .modelo(veiculo.getModelo()) // Corrigido de getModel() para getModelo()
                    .ano(veiculo.getAno())
                    .clienteId(veiculo.getCliente().getId())
                    .clienteNome(veiculo.getCliente().getNome())
                    .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> createVeiculo(@RequestBody Veiculo veiculo) {
        Veiculo savedVeiculo = veiculoService.save(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(VeiculoResponse.builder()
            .id(savedVeiculo.getId())
            .placa(savedVeiculo.getPlaca())
            .marca(savedVeiculo.getMarca())
            .modelo(savedVeiculo.getModelo()) // Corrigido de getModel() para getModelo()
            .ano(savedVeiculo.getAno())
            .clienteId(savedVeiculo.getCliente().getId())
            .clienteNome(savedVeiculo.getCliente().getNome())
            .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> updateVeiculo(@PathVariable UUID id, @RequestBody Veiculo veiculo) {
        try {
            Veiculo updatedVeiculo = veiculoService.update(id, veiculo);
            return ResponseEntity.ok(VeiculoResponse.builder()
                .id(updatedVeiculo.getId())
                .placa(updatedVeiculo.getPlaca())
                .marca(updatedVeiculo.getMarca())
                .modelo(updatedVeiculo.getModelo()) // Corrigido de getModel() para getModelo()
                .ano(updatedVeiculo.getAno())
                .clienteId(updatedVeiculo.getCliente().getId())
                .clienteNome(updatedVeiculo.getCliente().getNome())
                .build());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeiculo(@PathVariable UUID id) {
        if (!veiculoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        veiculoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}