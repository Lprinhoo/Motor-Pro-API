package org.example.service;

import org.example.model.Servico;
import org.example.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> findAll() {
        return servicoRepository.findAll();
    }

    public Optional<Servico> findById(UUID id) {
        return servicoRepository.findById(id);
    }

    @Transactional
    public Servico save(Servico servico) {
        return servicoRepository.save(servico);
    }

    @Transactional
    public Servico update(UUID id, Servico servicoDetails) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servico not found with id " + id));

        servico.setNome(servicoDetails.getNome());
        servico.setDescricao(servicoDetails.getDescricao());
        servico.setPreco(servicoDetails.getPreco());
        servico.setOficina(servicoDetails.getOficina());

        return servicoRepository.save(servico);
    }

    @Transactional
    public void deleteById(UUID id) {
        servicoRepository.deleteById(id);
    }
}
