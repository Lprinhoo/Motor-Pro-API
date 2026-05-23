package org.example.service;

import org.example.model.ItemPeca;
import org.example.repository.ItemPecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemPecaService {

    @Autowired
    private ItemPecaRepository itemPecaRepository;

    public List<ItemPeca> findAll() {
        return itemPecaRepository.findAll();
    }

    public Optional<ItemPeca> findById(UUID id) {
        return itemPecaRepository.findById(id);
    }

    @Transactional
    public ItemPeca save(ItemPeca itemPeca) {
        return itemPecaRepository.save(itemPeca);
    }

    @Transactional
    public ItemPeca update(UUID id, ItemPeca itemPecaDetails) {
        ItemPeca itemPeca = itemPecaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ItemPeca not found with id " + id));

        itemPeca.setQuantidade(itemPecaDetails.getQuantidade());
        itemPeca.setPrecoUnitario(itemPecaDetails.getPrecoUnitario());
        itemPeca.setOrdemServico(itemPecaDetails.getOrdemServico());
        itemPeca.setPeca(itemPecaDetails.getPeca());

        return itemPecaRepository.save(itemPeca);
    }

    @Transactional
    public void deleteById(UUID id) {
        itemPecaRepository.deleteById(id);
    }
}
