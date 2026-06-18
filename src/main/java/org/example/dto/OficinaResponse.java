package org.example.dto;

import java.util.List;
import java.util.UUID;

public record OficinaResponse(
        UUID id,
        String nome,
        String endereco,
        List<ContatoResponse> contatos
) {}
