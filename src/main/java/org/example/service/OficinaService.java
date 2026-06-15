package org.example.service;

import org.example.dto.OficinaRequest;
import org.example.model.Oficina;
import org.example.model.User;
import org.example.repository.OficinaRepository;
import org.example.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OficinaService {

    private final OficinaRepository oficinaRepository;
    private final UserRepository userRepository;

    public OficinaService(OficinaRepository oficinaRepository, UserRepository userRepository) {
        this.oficinaRepository = oficinaRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    public Oficina criar(OficinaRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (user.getOficina() != null) {
            throw new RuntimeException("Você já possui uma oficina cadastrada.");
        }

        Oficina oficina = Oficina.builder()
                .nome(request.nome())
                .endereco(request.endereco())
                .telefone(request.telefone())
                .email(request.email())
                .build();

        oficina = oficinaRepository.save(oficina);

        user.setOficina(oficina);
        user.setOwner(true);
        userRepository.save(user);

        return oficina;
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    public List<Oficina> findAll() {
        return oficinaRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    public Optional<Oficina> findById(UUID id) {
        return oficinaRepository.findById(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    public Optional<Oficina> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getOficina);
    }

    @Transactional
    @PreAuthorize("hasRole('OWNER')")
    public Oficina update(UUID id, OficinaRequest request, String username) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada."));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (user.getOficina() == null || !user.getOficina().getId().equals(id)) {
            throw new RuntimeException("Você não tem permissão para editar esta oficina.");
        }

        oficina.setNome(request.nome());
        oficina.setEndereco(request.endereco());
        oficina.setTelefone(request.telefone());
        oficina.setEmail(request.email());

        return oficinaRepository.save(oficina);
    }

    @Transactional
    @PreAuthorize("hasRole('OWNER')")
    public void delete(UUID id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (user.getOficina() == null || !user.getOficina().getId().equals(id)) {
            throw new RuntimeException("Você não tem permissão para excluir esta oficina.");
        }

        user.setOficina(null);
        user.setOwner(false);
        userRepository.save(user);

        oficinaRepository.deleteById(id);
    }
}