package org.example.dto;

public record OficinaRequest(
        String nome,
        String endereco,
        String telefone,
        String email
) {}
