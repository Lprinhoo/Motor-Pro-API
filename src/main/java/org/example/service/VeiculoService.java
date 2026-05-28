package org.example.service;

import org.example.model.Veiculo;
import org.example.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    public List<Veiculo> findAll() {
        return veiculoRepository.findAll();
    }

    public Optional<Veiculo> findById(UUID id) {
        return veiculoRepository.findById(id);
    }

    public List<Veiculo> findAllByOficina(UUID oficinaId) {
        return veiculoRepository.findByOficinaId(oficinaId);
    }

    @Transactional
    public Veiculo save(Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public Veiculo update(UUID id, Veiculo veiculoDetails) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veiculo not found with id " + id));

        veiculo.setPlaca(veiculoDetails.getPlaca());
        veiculo.setMarca(veiculoDetails.getMarca());
        veiculo.setModelo(veiculoDetails.getModelo());
        veiculo.setAno(veiculoDetails.getAno());
        veiculo.setCor(veiculoDetails.getCor());
        veiculo.setKm(veiculoDetails.getKm());
        veiculo.setCliente(veiculoDetails.getCliente());

        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public void deleteById(UUID id) {
        veiculoRepository.deleteById(id);
    }
}