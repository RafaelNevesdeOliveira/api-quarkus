package br.edu.fiap.banco.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade que representa o titular cadastrado na agência.
 * CPF identifica a pessoa no negócio; o ID identifica a linha no banco.
 * As anotações @Entity e @Table são Jakarta Persistence: mapeiam classe e tabela
 * exatamente como numa aplicação Spring Data JPA.
 */
@Entity
@Table(name = "pessoas")
public class Pessoa {

    // @Id indica a chave primária; @GeneratedValue(IDENTITY) usa o ID
    // gerado pelo PostgreSQL. As mesmas anotações são usadas no Spring JPA.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column espelha restrições de coluna; também é Jakarta JPA no Spring.
    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, length = 160)
    private String email;

    protected Pessoa() {
        // Uso exclusivo do JPA.
    }

    public Pessoa(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }
}
