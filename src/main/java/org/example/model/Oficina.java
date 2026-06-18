package org.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "oficinas")
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    private String endereco;

    @OneToMany(mappedBy = "oficina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();

    public Oficina() {}

    public Oficina(UUID id, String nome, String endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public List<Contato> getContatos() { return contatos; }
    public void setContatos(List<Contato> contatos) { this.contatos = contatos; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String nome;
        private String endereco;

        public Builder id(UUID id)         { this.id = id; return this; }
        public Builder nome(String nome)   { this.nome = nome; return this; }
        public Builder endereco(String e)  { this.endereco = e; return this; }

        public Oficina build() {
            return new Oficina(id, nome, endereco);
        }
    }
}
