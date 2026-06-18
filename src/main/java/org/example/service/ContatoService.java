package org.example.service;

import org.example.dto.ContatoRequest;
import org.example.exception.ForbiddenException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Contato;
import org.example.model.Oficina;
import org.example.model.User;
import org.example.repository.ContatoRepository;
import org.example.repository.OficinaRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final OficinaRepository oficinaRepository;
    private final UserRepository userRepository;

    public ContatoService(ContatoRepository contatoRepository,
                          OficinaRepository oficinaRepository,
                          UserRepository userRepository) {
        this.contatoRepository = contatoRepository;
        this.oficinaRepository = oficinaRepository;
        this.userRepository = userRepository;
    }

    private void verificarPropriedade(UUID oficinaId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        if (user.getOficina() == null || !user.getOficina().getId().equals(oficinaId)) {
            throw new ForbiddenException("Você não tem permissão para gerenciar contatos desta oficina.");
        }
    }

    public List<Contato> listar(UUID oficinaId) {
        if (!oficinaRepository.existsById(oficinaId)) {
            throw new ResourceNotFoundException("Oficina não encontrada.");
        }
        return contatoRepository.findByOficinaId(oficinaId);
    }

    @Transactional
    public Contato adicionar(UUID oficinaId, ContatoRequest request, String username) {
        verificarPropriedade(oficinaId, username);
        Oficina oficina = oficinaRepository.findById(oficinaId)
                .orElseThrow(() -> new ResourceNotFoundException("Oficina não encontrada."));
        Contato contato = new Contato(request.tipo(), request.valor(), oficina);
        return contatoRepository.save(contato);
    }

    @Transactional
    public Contato atualizar(UUID oficinaId, UUID contatoId, ContatoRequest request, String username) {
        verificarPropriedade(oficinaId, username);
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado."));
        if (!contato.getOficina().getId().equals(oficinaId)) {
            throw new ForbiddenException("Este contato não pertence a esta oficina.");
        }
        contato.setTipo(request.tipo());
        contato.setValor(request.valor());
        return contatoRepository.save(contato);
    }

    @Transactional
    public void deletar(UUID oficinaId, UUID contatoId, String username) {
        verificarPropriedade(oficinaId, username);
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado."));
        if (!contato.getOficina().getId().equals(oficinaId)) {
            throw new ForbiddenException("Este contato não pertence a esta oficina.");
        }
        contatoRepository.delete(contato);
    }
}
