package org.example.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.validation.Valid;
import org.example.dto.AuthResponse;
import org.example.dto.GoogleLoginRequest;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.GoogleAuthService;
import org.example.service.UserService; // Assumindo que você tem um UserService
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final GoogleAuthService googleAuthService;
    private final UserService userService; // Injetar UserService

    public AuthController(AuthService authService, GoogleAuthService googleAuthService, UserService userService) {
        this.authService = authService;
        this.googleAuthService = googleAuthService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        String token = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.authenticateUser(request.username(), request.password());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        // 1. Verificar o ID Token do Google
        GoogleIdToken.Payload payload = googleAuthService.verifyGoogleIdToken(request.idToken());

        // 2. Extrair informações do usuário do payload
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture"); // URL da foto de perfil
        String googleId = payload.getSubject(); // ID único do usuário no Google

        // 3. Encontrar ou criar o usuário no seu sistema
        // O UserService precisará de um método findOrCreateGoogleUser
        User user = userService.findOrCreateGoogleUser(email, name, pictureUrl, googleId);

        // 4. Gerar um JWT para o usuário autenticado
        // Assumindo que authService.generateToken(username) existe ou você tem um JwtService
        String token = authService.generateToken(user.getUsername()); // Usar o username (email) do usuário

        // 5. Retornar o token para o cliente
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
