package org.example.controller;

import org.example.model.Veiculo;
import org.example.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    public ResponseEntity<List<Veiculo>> getAllVeiculos() {
        List<Veiculo> veiculos = veiculoService.findAll();
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> getVeiculoById(@PathVariable UUID id) {
        return veiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Veiculo> createVeiculo(@RequestBody Veiculo veiculo) {
        Veiculo savedVeiculo = veiculoService.save(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVeiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> updateVeiculo(@PathVariable UUID id, @RequestBody Veiculo veiculo) {
        try {
            Veiculo updatedVeiculo = veiculoService.update(id, veiculo);
            return ResponseEntity.ok(updatedVeiculo);
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
