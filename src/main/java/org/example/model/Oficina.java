package org.example.model;

import jakarta.persistence.*;
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
    private String telefone;
    private String email;

    public Oficina() {}

    public Oficina(UUID id, String nome, String endereco, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String nome;
        private String endereco;
        private String telefone;
        private String email;

        public Builder id(UUID id)             { this.id = id; return this; }
        public Builder nome(String nome)       { this.nome = nome; return this; }
        public Builder endereco(String e)      { this.endereco = e; return this; }
        public Builder telefone(String t)      { this.telefone = t; return this; }
        public Builder email(String email)     { this.email = email; return this; }

        public Oficina build() {
            return new Oficina(id, nome, endereco, telefone, email);
        }
    }
}