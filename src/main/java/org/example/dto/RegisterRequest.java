package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Role;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nome;
    private String email;
    private String senha;
    private Role role;
    private UUID oficinaId; // Para relacionar o usuário a uma oficina existente
}
