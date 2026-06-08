package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
// Importar a exceção customizada, se existir. Se não, precisaria ser criada.
// import org.example.exception.UsernameAlreadyExistsException; // Exemplo de import para exceção customizada

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
            // Usando uma RuntimeException genérica por enquanto, se UsernameAlreadyExistsException não for customizada.
            // Se você tiver uma exceção customizada, use-a aqui.
            throw new UsernameAlreadyExistsException("Nome de usuário já está em uso.");
        }
        return userRepository.save(new User(username, passwordEncoder.encode(password), email));
    }

    public String authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Otimização: Extrai o User diretamente do objeto Authentication
        // Como sua entidade User implementa UserDetails, você pode fazer um cast
        User user = (User) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }
}