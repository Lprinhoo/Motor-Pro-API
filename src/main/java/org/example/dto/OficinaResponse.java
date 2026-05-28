package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OficinaResponse {
    private UUID id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
}