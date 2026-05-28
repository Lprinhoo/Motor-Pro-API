package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClienteResponse {
    private UUID id;
    private String nome;
    private String cpfCnpj;
    private String email;
    private String telefone;
    private String endereco;
}