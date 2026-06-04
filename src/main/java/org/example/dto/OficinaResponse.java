package org.example.dto;

import java.util.UUID;

public record OficinaResponse(
        UUID id,
        String nome,
        String endereco,
        String telefone,
        String email
) {
}