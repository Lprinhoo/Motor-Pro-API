package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 14, unique = true) // CPF tem 11 dígitos, mas pode ter formatação
    private String cpf;

    @Column(length = 18, unique = true) // CNPJ tem 14 dígitos, mas pode ter formatação
    private String cnpj;

    @Column(length = 20)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oficina_id", nullable = false)
    private Oficina oficina;
}
