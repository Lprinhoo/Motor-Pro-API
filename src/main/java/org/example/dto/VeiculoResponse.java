package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class VeiculoResponse {
    private UUID id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private UUID clienteId;
    private String clienteNome;
}