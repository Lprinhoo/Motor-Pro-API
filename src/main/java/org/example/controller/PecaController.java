package org.example.controller;

import org.example.model.Peca;
import org.example.service.PecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    @Autowired
    private PecaService pecaService;

    @GetMapping
    public ResponseEntity<List<Peca>> getAllPecas() {
        List<Peca> pecas = pecaService.findAll();
        return ResponseEntity.ok(pecas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peca> getPecaById(@PathVariable UUID id) {
        return pecaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Peca> createPeca(@RequestBody Peca peca) {
        Peca savedPeca = pecaService.save(peca);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPeca);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Peca> updatePeca(@PathVariable UUID id, @RequestBody Peca peca) {
        try {
            Peca updatedPeca = pecaService.update(id, peca);
            return ResponseEntity.ok(updatedPeca);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeca(@PathVariable UUID id) {
        if (!pecaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        pecaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
