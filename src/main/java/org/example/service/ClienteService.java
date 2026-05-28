package org.example.service;

import org.example.model.Cliente;
import org.example.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(UUID id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> findAllByOficina(UUID oficinaId) {
        return clienteRepository.findByOficinaId(oficinaId);
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente update(UUID id, Cliente clienteDetails) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente not found with id " + id));

        cliente.setNome(clienteDetails.getNome());
        cliente.setCpf(clienteDetails.getCpf());
        cliente.setCnpj(clienteDetails.getCnpj());
        cliente.setTelefone(clienteDetails.getTelefone());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setEndereco(clienteDetails.getEndereco());
        cliente.setOficina(clienteDetails.getOficina());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deleteById(UUID id) {
        clienteRepository.deleteById(id);
    }
}