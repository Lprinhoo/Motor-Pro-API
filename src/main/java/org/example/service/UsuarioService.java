package org.example.service;

import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(UUID id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(UUID id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found with id " + id));

        usuario.setNome(usuarioDetails.getNome());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setSenha(usuarioDetails.getSenha()); // Lembre-se de criptografar em um cenário real
        usuario.setRole(usuarioDetails.getRole());
        usuario.setOficina(usuarioDetails.getOficina());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteById(UUID id) {
        usuarioRepository.deleteById(id);
    }
}
