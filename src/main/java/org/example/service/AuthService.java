package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.example.service.UsernameAlreadyExistsException; // Adicionado import explícito

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
            throw new UsernameAlreadyExistsException("Username already exists"); // Usando a exceção específica
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

    // Novo método para solicitar redefinição de senha
    public void requestPasswordReset(String email) {
        // Para este método funcionar, o UserRepository precisaria de um findByEmail
        // Optional<User> userOptional = userRepository.findByEmail(email);
        // Se não houver findByEmail, você precisaria adicionar um ao UserRepository
        // Por enquanto, vamos simular ou lançar uma exceção se o email não for encontrado
        // Para fins de demonstração, vamos apenas logar.
        System.out.println("Funcionalidade de redefinição de senha não implementada completamente no backend.");
        System.out.println("Tentativa de redefinição para o email: " + email);
    }
}