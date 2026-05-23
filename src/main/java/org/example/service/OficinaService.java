package org.example.service;

import org.example.model.Oficina;
import org.example.repository.OficinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OficinaService {

    @Autowired
    private OficinaRepository oficinaRepository;

    public List<Oficina> findAll() {
        return oficinaRepository.findAll();
    }

    public Optional<Oficina> findById(UUID id) {
        return oficinaRepository.findById(id);
    }

    @Transactional
    public Oficina save(Oficina oficina) {
        return oficinaRepository.save(oficina);
    }

    @Transactional
    public Oficina update(UUID id, Oficina oficinaDetails) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oficina not found with id " + id)); // Tratar com exceção mais específica ou ResponseEntity

        oficina.setNome(oficinaDetails.getNome());
        oficina.setCnpj(oficinaDetails.getCnpj());
        oficina.setEndereco(oficinaDetails.getEndereco());
        oficina.setTelefone(oficinaDetails.getTelefone());

        return oficinaRepository.save(oficina);
    }

    @Transactional
    public void deleteById(UUID id) {
        oficinaRepository.deleteById(id);
    }
}
