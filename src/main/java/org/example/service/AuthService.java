package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional; // Importar Optional

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(new User(username, passwordEncoder.encode(password), email));
    }

    public String authenticateUser(String username, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));
        return jwtService.generateToken(user);
    }

    // Novo método para solicitar redefinição de senha
    public void requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            // Em um cenário real, aqui você geraria um token de redefinição
            // e enviaria um e-mail para o usuário com um link contendo esse token.
            System.out.println("Link de redefinição de senha simulado enviado para: " + email);
            // Exemplo: emailService.sendPasswordResetEmail(userOptional.get(), resetToken);
        } else {
            // Por segurança, não informamos se o e-mail não foi encontrado.
            System.out.println("Tentativa de redefinição de senha para e-mail não registrado ou mensagem genérica enviada.");
        }
    }
}
