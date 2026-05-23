package org.example.service;

import org.example.model.OrdemServico;
import org.example.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    public List<OrdemServico> findAll() {
        return ordemServicoRepository.findAll();
    }

    public Optional<OrdemServico> findById(UUID id) {
        return ordemServicoRepository.findById(id);
    }

    @Transactional
    public OrdemServico save(OrdemServico ordemServico) {
        return ordemServicoRepository.save(ordemServico);
    }

    @Transactional
    public OrdemServico update(UUID id, OrdemServico ordemServicoDetails) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdemServico not found with id " + id));

        ordemServico.setNumero(ordemServicoDetails.getNumero());
        ordemServico.setStatus(ordemServicoDetails.getStatus());
        ordemServico.setDataAbertura(ordemServicoDetails.getDataAbertura());
        ordemServico.setDataConclusao(ordemServicoDetails.getDataConclusao());
        ordemServico.setValorTotal(ordemServicoDetails.getValorTotal());
        ordemServico.setVeiculo(ordemServicoDetails.getVeiculo());
        // Itens de serviço e peça podem precisar de lógica de atualização mais complexa

        return ordemServicoRepository.save(ordemServico);
    }

    @Transactional
    public void deleteById(UUID id) {
        ordemServicoRepository.deleteById(id);
    }
}
