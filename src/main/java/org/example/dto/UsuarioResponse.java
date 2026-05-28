package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.model.Role;

import java.util.UUID;

@Data
@Builder
public class UsuarioResponse {
    private UUID id;
    private String nome;
    private String email;
    private Role role;
    private UUID oficinaId;
    private String oficinaNome;
}