package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ServicoResponse {
    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
}