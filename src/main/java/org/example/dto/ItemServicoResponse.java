package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ItemServicoResponse {
    private UUID id;
    private UUID servicoId;
    private String servicoNome;
    private UUID ordemServicoId;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}