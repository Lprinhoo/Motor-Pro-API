package org.example.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ServicoResponse(
        UUID id,
        String nome,
        String descricao,
        BigDecimal valor,
        Integer tempoMedioEmMinutos,
        OficinaResponse oficina
) {}