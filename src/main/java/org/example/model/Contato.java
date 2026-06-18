package org.example.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "contatos")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoContato tipo;

    @Column(nullable = false)
    private String valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oficina_id", nullable = false)
    private Oficina oficina;

    public Contato() {}

    public Contato(TipoContato tipo, String valor, Oficina oficina) {
        this.tipo = tipo;
        this.valor = valor;
        this.oficina = oficina;
    }

    public UUID getId() { return id; }
    public TipoContato getTipo() { return tipo; }
    public void setTipo(TipoContato tipo) { this.tipo = tipo; }
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
    public Oficina getOficina() { return oficina; }
    public void setOficina(Oficina oficina) { this.oficina = oficina; }
}
