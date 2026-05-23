package org.example.service;

import org.example.model.Peca;
import org.example.repository.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PecaService {

    @Autowired
    private PecaRepository pecaRepository;

    public List<Peca> findAll() {
        return pecaRepository.findAll();
    }

    public Optional<Peca> findById(UUID id) {
        return pecaRepository.findById(id);
    }

    @Transactional
    public Peca save(Peca peca) {
        return pecaRepository.save(peca);
    }

    @Transactional
    public Peca update(UUID id, Peca pecaDetails) {
        Peca peca = pecaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Peca not found with id " + id));

        peca.setNome(pecaDetails.getNome());
        peca.setCodigo(pecaDetails.getCodigo());
        peca.setPreco(pecaDetails.getPreco());
        peca.setEstoque(pecaDetails.getEstoque());
        peca.setOficina(pecaDetails.getOficina());

        return pecaRepository.save(peca);
    }

    @Transactional
    public void deleteById(UUID id) {
        pecaRepository.deleteById(id);
    }
}
