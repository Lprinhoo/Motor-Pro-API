package org.example.service;

import org.example.model.ItemServico;
import org.example.repository.ItemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemServicoService {

    @Autowired
    private ItemServicoRepository itemServicoRepository;

    public List<ItemServico> findAll() {
        return itemServicoRepository.findAll();
    }

    public Optional<ItemServico> findById(UUID id) {
        return itemServicoRepository.findById(id);
    }

    @Transactional
    public ItemServico save(ItemServico itemServico) {
        return itemServicoRepository.save(itemServico);
    }

    @Transactional
    public ItemServico update(UUID id, ItemServico itemServicoDetails) {
        ItemServico itemServico = itemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ItemServico not found with id " + id));

        itemServico.setQuantidade(itemServicoDetails.getQuantidade());
        itemServico.setPrecoUnitario(itemServicoDetails.getPrecoUnitario());
        itemServico.setOrdemServico(itemServicoDetails.getOrdemServico());
        itemServico.setServico(itemServicoDetails.getServico());

        return itemServicoRepository.save(itemServico);
    }

    @Transactional
    public void deleteById(UUID id) {
        itemServicoRepository.deleteById(id);
    }
}
