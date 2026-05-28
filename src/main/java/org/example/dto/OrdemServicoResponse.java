package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrdemServicoResponse {
    private UUID id;
    private String numero;
    private String status;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataConclusao;
    private BigDecimal valorTotal;
    private UUID veiculoId;
    private String veiculoPlaca;
}