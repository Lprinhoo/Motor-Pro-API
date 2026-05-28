package org.example.controller;

import org.example.dto.ClienteResponse;
import org.example.model.Cliente;
import org.example.model.Usuario; // Importar Usuario
import org.example.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Importar Authentication
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAllClientes(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(
            clienteService.findAllByOficina(usuario.getOficina().getId()).stream()
                .map(cliente -> ClienteResponse.builder()
                    .id(cliente.getId())
                    .nome(cliente.getNome())
                    .cpfCnpj(cliente.getCpf() != null && !cliente.getCpf().isEmpty() ? cliente.getCpf() : cliente.getCnpj()) // Corrigido o mapeamento de cpfCnpj
                    .email(cliente.getEmail())
                    .telefone(cliente.getTelefone())
                    .endereco(cliente.getEndereco())
                    .build())
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getClienteById(@PathVariable UUID id) {
        return clienteService.findById(id)
                .map(cliente -> ResponseEntity.ok(ClienteResponse.builder()
                    .id(cliente.getId())
                    .nome(cliente.getNome())
                    .cpfCnpj(cliente.getCpf() != null && !cliente.getCpf().isEmpty() ? cliente.getCpf() : cliente.getCnpj()) // Corrigido o mapeamento de cpfCnpj
                    .email(cliente.getEmail())
                    .telefone(cliente.getTelefone())
                    .endereco(cliente.getEndereco())
                    .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        Cliente savedCliente = clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable UUID id, @RequestBody Cliente cliente) {
        try {
            Cliente updatedCliente = clienteService.update(id, cliente);
            return ResponseEntity.ok(updatedCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable UUID id) {
        if (!clienteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}