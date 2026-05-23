package org.example.controller;

import org.example.model.ItemServico;
import org.example.service.ItemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/itens-servico")
public class ItemServicoController {

    @Autowired
    private ItemServicoService itemServicoService;

    @GetMapping
    public ResponseEntity<List<ItemServico>> getAllItensServico() {
        List<ItemServico> itensServico = itemServicoService.findAll();
        return ResponseEntity.ok(itensServico);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemServico> getItemServicoById(@PathVariable UUID id) {
        return itemServicoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ItemServico> createItemServico(@RequestBody ItemServico itemServico) {
        ItemServico savedItemServico = itemServicoService.save(itemServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItemServico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemServico> updateItemServico(@PathVariable UUID id, @RequestBody ItemServico itemServico) {
        try {
            ItemServico updatedItemServico = itemServicoService.update(id, itemServico);
            return ResponseEntity.ok(updatedItemServico);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemServico(@PathVariable UUID id) {
        if (!itemServicoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        itemServicoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
