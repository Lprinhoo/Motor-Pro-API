package org.example.service;

import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(UUID id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> findAllByOficina(UUID oficinaId) {
        return usuarioRepository.findByOficinaId(oficinaId);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        // It's good practice to encode the password here as well if saving new users
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(UUID id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found with id " + id));

        usuario.setNome(usuarioDetails.getNome());
        usuario.setEmail(usuarioDetails.getEmail());
        // Encode the password before saving
        usuario.setSenha(passwordEncoder.encode(usuarioDetails.getSenha()));
        usuario.setRole(usuarioDetails.getRole());
        usuario.setOficina(usuarioDetails.getOficina());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteById(UUID id) {
        usuarioRepository.deleteById(id);
    }
}