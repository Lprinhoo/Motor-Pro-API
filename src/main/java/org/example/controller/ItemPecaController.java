package org.example.controller;

import org.example.dto.ItemPecaResponse;
import org.example.model.ItemPeca;
import org.example.service.ItemPecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/itens-peca")
public class ItemPecaController {

    @Autowired
    private ItemPecaService itemPecaService;

    @GetMapping
    public ResponseEntity<List<ItemPecaResponse>> getAllItensPeca() {
        return ResponseEntity.ok(
            itemPecaService.findAll().stream()
                .map(itemPeca -> ItemPecaResponse.builder()
                    .id(itemPeca.getId())
                    .pecaId(itemPeca.getPeca().getId())
                    .pecaNome(itemPeca.getPeca().getNome())
                    .ordemServicoId(itemPeca.getOrdemServico().getId())
                    .quantidade(itemPeca.getQuantidade())
                    .precoUnitario(itemPeca.getPrecoUnitario())
                    .build())
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPecaResponse> getItemPecaById(@PathVariable UUID id) {
        return itemPecaService.findById(id)
                .map(itemPeca -> ResponseEntity.ok(ItemPecaResponse.builder()
                    .id(itemPeca.getId())
                    .pecaId(itemPeca.getPeca().getId())
                    .pecaNome(itemPeca.getPeca().getNome())
                    .ordemServicoId(itemPeca.getOrdemServico().getId())
                    .quantidade(itemPeca.getQuantidade())
                    .precoUnitario(itemPeca.getPrecoUnitario())
                    .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ItemPeca> createItemPeca(@RequestBody ItemPeca itemPeca) {
        ItemPeca savedItemPeca = itemPecaService.save(itemPeca);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItemPeca);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemPeca> updateItemPeca(@PathVariable UUID id, @RequestBody ItemPeca itemPeca) {
        try {
            ItemPeca updatedItemPeca = itemPecaService.update(id, itemPeca);
            return ResponseEntity.ok(updatedItemPeca);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemPeca(@PathVariable UUID id) {
        if (!itemPecaService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        itemPecaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}