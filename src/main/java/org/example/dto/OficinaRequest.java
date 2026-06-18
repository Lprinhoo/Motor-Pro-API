package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OficinaRequest(
        @NotBlank(message = "O nome da oficina não pode estar em branco")
        @Size(min = 3, max = 100, message = "O nome da oficina deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "O endereço da oficina não pode estar em branco")
        @Size(min = 5, max = 200, message = "O endereço da oficina deve ter entre 5 e 200 caracteres")
        String endereco,


        @Size(min = 8, max = 20, message = "O telefone da oficina deve ter entre 8 e 20 caracteres")
        String telefone,

        @Email(message = "O email da oficina deve ser válido")
        String email
) {}
