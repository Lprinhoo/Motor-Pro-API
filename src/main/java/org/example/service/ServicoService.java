package org.example.service;

import org.example.dto.ServicoRequest;
import org.example.model.Oficina;
import org.example.model.Servico;
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
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final UserRepository userRepository;
    private final OficinaRepository oficinaRepository; // Injetar OficinaRepository para buscar a oficina

    public ServicoService(ServicoRepository servicoRepository, UserRepository userRepository, OficinaRepository oficinaRepository) {
        this.servicoRepository = servicoRepository;
        this.userRepository = userRepository;
        this.oficinaRepository = oficinaRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('OWNER')")
    public Servico createServico(ServicoRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Oficina oficina = user.getOficina();
        if (oficina == null) {
            throw new RuntimeException("O usuário não possui uma oficina associada.");
        }

        Servico servico = Servico.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .valor(request.valor())
                .tempoMedioEmMinutos(request.tempoMedioEmMinutos())
                .oficina(oficina)
                .build();

        return servicoRepository.save(servico);
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER')") // Qualquer usuário pode ver os serviços de uma oficina
    public List<Servico> getServicosByOficina(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Oficina oficina = user.getOficina();
        if (oficina == null) {
            throw new RuntimeException("O usuário não possui uma oficina associada.");
        }
        return servicoRepository.findByOficinaId(oficina.getId());
    }

    @PreAuthorize("hasAnyRole('USER', 'OWNER')")
    public Optional<Servico> getServicoById(UUID id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Oficina oficina = user.getOficina();
        if (oficina == null) {
            throw new RuntimeException("O usuário não possui uma oficina associada.");
        }

        return servicoRepository.findById(id)
                .filter(servico -> servico.getOficina().getId().equals(oficina.getId())); // Garante que o serviço pertence à oficina do usuário
    }

    @Transactional
    @PreAuthorize("hasRole('OWNER')")
    public Servico updateServico(UUID id, ServicoRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Oficina oficina = user.getOficina();
        if (oficina == null) {
            throw new RuntimeException("O usuário não possui uma oficina associada.");
        }

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));

        if (!servico.getOficina().getId().equals(oficina.getId())) {
            throw new RuntimeException("Você não tem permissão para editar este serviço.");
        }

        servico.setNome(request.nome());
        servico.setDescricao(request.descricao());
        servico.setValor(request.valor());
        servico.setTempoMedioEmMinutos(request.tempoMedioEmMinutos());

        return servicoRepository.save(servico);
    }

    @Transactional
    @PreAuthorize("hasRole('OWNER')")
    public void deleteServico(UUID id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Oficina oficina = user.getOficina();
        if (oficina == null) {
            throw new RuntimeException("O usuário não possui uma oficina associada.");
        }

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));

        if (!servico.getOficina().getId().equals(oficina.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir este serviço.");
        }

        servicoRepository.delete(servico);
    }
}
