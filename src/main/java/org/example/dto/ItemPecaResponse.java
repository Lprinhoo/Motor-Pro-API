package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ItemPecaResponse {
    private UUID id;
    private UUID pecaId;
    private String pecaNome;
    private UUID ordemServicoId;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}