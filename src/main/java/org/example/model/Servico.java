package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private Integer tempoMedioEmMinutos; // Tempo médio em minutos

    @JsonIgnore  // ← adicionar essa linha
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oficina_id", nullable = false)
    private Oficina oficina;

    public Servico() {}

    public Servico(String nome, String descricao, BigDecimal valor, Integer tempoMedioEmMinutos, Oficina oficina) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.tempoMedioEmMinutos = tempoMedioEmMinutos;
        this.oficina = oficina;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public Integer getTempoMedioEmMinutos() { return tempoMedioEmMinutos; }
    public void setTempoMedioEmMinutos(Integer tempoMedioEmMinutos) { this.tempoMedioEmMinutos = tempoMedioEmMinutos; }

    public Oficina getOficina() { return oficina; }
    public void setOficina(Oficina oficina) { this.oficina = oficina; }

    // Builder pattern (opcional, mas útil)
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String nome;
        private String descricao;
        private BigDecimal valor;
        private Integer tempoMedioEmMinutos;
        private Oficina oficina;

        public Builder nome(String nome) { this.nome = nome; return this; }
        public Builder descricao(String descricao) { this.descricao = descricao; return this; }
        public Builder valor(BigDecimal valor) { this.valor = valor; return this; }
        public Builder tempoMedioEmMinutos(Integer tempoMedioEmMinutos) { this.tempoMedioEmMinutos = tempoMedioEmMinutos; return this; }
        public Builder oficina(Oficina oficina) { this.oficina = oficina; return this; }

        public Servico build() {
            return new Servico(nome, descricao, valor, tempoMedioEmMinutos, oficina);
        }
    }
}