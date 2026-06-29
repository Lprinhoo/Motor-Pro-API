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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final GoogleAuthService googleAuthService;

    public AuthController(AuthService authService, GoogleAuthService googleAuthService) {
        this.authService = authService;
        this.googleAuthService = googleAuthService;
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

        // 3. Encontrar ou criar o usuário no seu sistema usando AuthService
        User user = authService.findOrCreateGoogleUser(email, name, pictureUrl, googleId);

        // 4. Gerar um JWT para o usuário autenticado
        String token = authService.generateToken(user); // Usar o método generateToken(User user) do AuthService

        // 5. Retornar o token para o cliente
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
