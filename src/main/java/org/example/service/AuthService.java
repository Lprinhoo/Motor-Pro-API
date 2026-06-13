package org.example.service;

import org.example.dto.RegisterRequest;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final OficinaService oficinaService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService,
                       OficinaService oficinaService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.oficinaService = oficinaService;
    }

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UsernameAlreadyExistsException("Nome de usuário já está em uso.");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UsernameAlreadyExistsException("Email já está em uso.");
        }

        User user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.email()
        );
        user = userRepository.save(user); // Salva o usuário inicial

        // Chama o OficinaService para criar a oficina e associá-la ao usuário.
        // O método 'criar' do OficinaService já lida com a associação, o setOwner(true)
        // e salva o usuário atualizado.
        oficinaService.criar(request.oficinaRequest(), user.getUsername());

        // Recarrega o usuário para garantir que o objeto retornado contenha
        // a Oficina associada e o status de proprietário atualizado.
        return userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar usuário após registro de oficina."));
    }

    public String authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }
}
