package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pecas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Peca {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oficina_id", nullable = false)
    private Oficina oficina;
}
