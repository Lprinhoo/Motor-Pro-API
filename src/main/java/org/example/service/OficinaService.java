package org.example.service;

import org.example.dto.OficinaRequest;
import org.example.exception.ForbiddenException;
import org.example.exception.OficinaAlreadyExistsException;
import org.example.exception.OficinaComServicosException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Oficina;
import org.example.model.User;
import org.example.repository.OficinaRepository;
import org.example.repository.ServicoRepository;
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
    private final ServicoRepository servicoRepository;

    public OficinaService(OficinaRepository oficinaRepository, UserRepository userRepository,
                           ServicoRepository servicoRepository) {
        this.oficinaRepository = oficinaRepository;
        this.userRepository = userRepository;
        this.servicoRepository = servicoRepository;
    }

    @Transactional
    public Oficina criar(OficinaRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (user.getOficina() != null) {
            throw new OficinaAlreadyExistsException("Você já possui uma oficina cadastrada.");
        }

        Oficina oficina = Oficina.builder()
                .nome(request.nome())
                .endereco(request.endereco())
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

    @Transactional // Added @Transactional to ensure lazy loading works
    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    public Optional<Oficina> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getOficina);
    }

    @Transactional
    @PreAuthorize("hasRole('OWNER')")
    public Oficina update(UUID id, OficinaRequest request, String username) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oficina não encontrada."));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (user.getOficina() == null || !user.getOficina().getId().equals(id)) {
            throw new ForbiddenException("Você não tem permissão para editar esta oficina.");
        }

        oficina.setNome(request.nome());
        oficina.setEndereco(request.endereco());

        return oficinaRepository.save(oficina);
    }

    @Transactional
    @PreAuthorize("hasRole('OWNER')")
    public void delete(UUID id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (user.getOficina() == null || !user.getOficina().getId().equals(id)) {
            throw new ForbiddenException("Você não tem permissão para excluir esta oficina.");
        }

        if (!servicoRepository.findByOficinaId(id).isEmpty()) {
            throw new OficinaComServicosException(
                    "Não é possível excluir a oficina enquanto houver serviços cadastrados. " +
                    "Remova todos os serviços antes de excluir a oficina.");
        }

        user.setOficina(null);
        user.setOwner(false);
        userRepository.save(user);

        oficinaRepository.deleteById(id);
    }
}