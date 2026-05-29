package org.example.controller;

import org.example.dto.EmailRequest; // Importar o novo DTO
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            var user = authService.registerUser(request.username(), request.email(), request.password());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuário " + user.getUsername() + " registrado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticateUser(request.username(), request.password());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Falha na autenticação: " + e.getMessage());
        }
    }

    // Novo endpoint para solicitar redefinição de senha
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        try {
            authService.requestPasswordReset(request.email());
            return ResponseEntity.ok("Se um usuário com este email existir, um link de redefinição de senha foi enviado.");
        } catch (RuntimeException e) {
            // Para segurança, geralmente retornamos uma mensagem genérica mesmo se o email não existir
            return ResponseEntity.ok("Se um usuário com este email existir, um link de redefinição de senha foi enviado.");
        }
    }
}
