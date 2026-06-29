package org.example.service;

import org.example.dto.RegisterRequest;
import org.example.exception.UsernameAlreadyExistsException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /**
     * Encontra um usuário existente pelo email/googleId ou cria um novo para login com Google.
     *
     * @param email O email do usuário do Google.
     * @param name O nome completo do usuário do Google.
     * @param pictureUrl A URL da foto de perfil do Google.
     * @param googleId O ID único do usuário fornecido pelo Google.
     * @return O objeto User encontrado ou criado.
     */
    @Transactional
    public User findOrCreateGoogleUser(String email, String name, String pictureUrl, String googleId) {
        // Tenta encontrar o usuário pelo googleId primeiro
        Optional<User> existingUser = userRepository.findByGoogleId(googleId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Atualiza informações se necessário (ex: foto de perfil, nome)
            user.setName(name);
            user.setPictureUrl(pictureUrl);
            userRepository.save(user);
            return user;
        } else {
            // Se não encontrou pelo googleId, tenta encontrar pelo email
            // Isso pode acontecer se o usuário já se registrou manualmente com o mesmo email
            // ou se o googleId foi adicionado posteriormente.
            Optional<User> userByEmail = userRepository.findByEmail(email);
            if (userByEmail.isPresent()) {
                User user = userByEmail.get();
                // Se o usuário existe e não é social, podemos associar o googleId
                if (!user.isSocialLogin()) {
                    user.setGoogleId(googleId);
                    user.setSocialLogin(true); // Marca como login social
                    user.setName(name);
                    user.setPictureUrl(pictureUrl);
                    // A senha original pode ser mantida, mas o login será via Google
                    userRepository.save(user);
                    return user;
                } else {
                    // Se já é social mas com outro googleId (improvável, mas possível em cenários complexos)
                    // Ou se o googleId não estava preenchido
                    user.setGoogleId(googleId);
                    user.setName(name);
                    user.setPictureUrl(pictureUrl);
                    userRepository.save(user);
                    return user;
                }
            } else {
                // Cria um novo usuário para o login social
                User newUser = new User(email, name, pictureUrl, googleId);
                // Definir roles padrão, se houver
                // newUser.setOwner(false); // Exemplo
                return userRepository.save(newUser);
            }
        }
    }

    // Método auxiliar para gerar token, se o AuthController precisar de um User object
    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }
}
