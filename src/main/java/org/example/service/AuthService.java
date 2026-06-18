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

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public String registerUser(RegisterRequest request) {
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
        userRepository.save(user);

        // Retorna o token JWT para que o frontend possa autenticar
        // imediatamente e chamar POST /api/oficinas na etapa seguinte
        return jwtService.generateToken(user);
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
