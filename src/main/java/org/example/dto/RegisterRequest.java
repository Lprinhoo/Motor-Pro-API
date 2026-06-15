package org.example.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern; // Import adicionado
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "O nome de usuário não pode estar em branco")
        @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres")
        String username,

        @NotBlank(message = "O email não pode estar em branco")
        @Email(message = "O email deve ser válido")
        String email,

        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
                message = "Senha deve ter 8+ chars, 1 maiúscula, 1 minúscula e 1 número"
        )
        String password,

        @Valid
        OficinaRequest oficinaRequest
) {}