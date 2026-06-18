package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.model.TipoContato;

public record ContatoRequest(
        @NotNull(message = "O tipo do contato é obrigatório")
        TipoContato tipo,

        @NotBlank(message = "O valor do contato não pode estar em branco")
        @Size(max = 100, message = "O valor do contato deve ter no máximo 100 caracteres")
        String valor
) {}
