package org.example.dto;

import org.example.model.TipoContato;
import java.util.UUID;

public record ContatoResponse(
        UUID id,
        TipoContato tipo,
        String valor
) {}
