package org.example.service;

import org.example.model.OrdemServico;
import org.example.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    public List<OrdemServico> findAllByOficina(UUID oficinaId) {
        return ordemServicoRepository.findByOficinaId(oficinaId);
    }

    @Transactional
    public OrdemServico save(OrdemServico ordemServico) {
        ordemServico.setValorTotal(calcularValorTotal(ordemServico));
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
        // Remove a linha que copia valorTotal diretamente de ordemServicoDetails
        // ordemServico.setValorTotal(ordemServicoDetails.getValorTotal());
        ordemServico.setVeiculo(ordemServicoDetails.getVeiculo());
        ordemServico.setItensPeca(ordemServicoDetails.getItensPeca()); // Certifique-se de que os itens são atualizados
        ordemServico.setItensServico(ordemServicoDetails.getItensServico()); // Certifique-se de que os itens são atualizados

        ordemServico.setValorTotal(calcularValorTotal(ordemServico)); // Recalcula o valor total após a atualização dos itens

        return ordemServicoRepository.save(ordemServico);
    }

    @Transactional
    public void deleteById(UUID id) {
        ordemServicoRepository.deleteById(id);
    }

    private BigDecimal calcularValorTotal(OrdemServico os) {
        BigDecimal totalPecas = os.getItensPeca() == null ? BigDecimal.ZERO :
            os.getItensPeca().stream()
                .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalServicos = os.getItensServico() == null ? BigDecimal.ZERO :
            os.getItensServico().stream()
                .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPecas.add(totalServicos);
    }
}