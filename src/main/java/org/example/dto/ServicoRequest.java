package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ServicoRequest(
        @NotBlank(message = "O nome do serviço não pode estar em branco")
        @Size(min = 3, max = 100, message = "O nome do serviço deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "A descrição do serviço não pode estar em branco")
        @Size(min = 10, max = 500, message = "A descrição do serviço deve ter entre 10 e 500 caracteres")
        String descricao,

        @NotNull(message = "O valor do serviço não pode ser nulo")
        @DecimalMin(value = "0.01", message = "O valor do serviço deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "O tempo médio do serviço não pode ser nulo")
        @Min(value = 1, message = "O tempo médio do serviço deve ser de no mínimo 1 minuto")
        Integer tempoMedioEmMinutos
) {}