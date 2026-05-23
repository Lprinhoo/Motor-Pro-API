package org.example.controller;

import org.example.model.Oficina;
import org.example.service.OficinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaController {

    @Autowired
    private OficinaService oficinaService;

    @GetMapping
    public ResponseEntity<List<Oficina>> getAllOficinas() {
        List<Oficina> oficinas = oficinaService.findAll();
        return ResponseEntity.ok(oficinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oficina> getOficinaById(@PathVariable UUID id) {
        return oficinaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Oficina> createOficina(@RequestBody Oficina oficina) {
        Oficina savedOficina = oficinaService.save(oficina);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOficina);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oficina> updateOficina(@PathVariable UUID id, @RequestBody Oficina oficina) {
        try {
            Oficina updatedOficina = oficinaService.update(id, oficina);
            return ResponseEntity.ok(updatedOficina);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOficina(@PathVariable UUID id) {
        if (!oficinaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        oficinaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
