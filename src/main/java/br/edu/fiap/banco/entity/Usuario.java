package br.edu.fiap.banco.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade de credencial usada para identificar quem acessará a API.
 *
 * <p>Ela é separada de {@link Pessoa}: pessoa é o titular do domínio bancário,
 * enquanto usuário é a identidade de acesso. O atributo {@code senha} nunca
 * guarda texto puro; depois do cadastro ele contém somente o hash BCrypt.</p>
 * As anotações @Entity e @Table são Jakarta JPA, iguais às usadas no
 * Spring Data JPA.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    // @Id, @GeneratedValue(IDENTITY) e @Column descrevem o mesmo mapeamento
    // JPA de chave e colunas em Quarkus e Spring.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    /**
     * Resultado BCrypt armazenado no banco.
     *
     * <p>{@link JsonIgnore} é uma defesa adicional. A API usa DTOs de resposta
     * sem senha, mas esta anotação também impede que o hash seja enviado caso
     * alguém serialize a entidade acidentalmente. Jackson aplica @JsonIgnore
     * da mesma forma quando usado pelo Spring.</p>
     */
    @JsonIgnore
    @Column(nullable = false, length = 60)
    private String senha;

    @Column(nullable = false)
    private boolean ativo = true;

    protected Usuario() {
        // Construtor exigido pelo JPA.
    }

    /** Cria o usuário já com a senha transformada pelo service. */
    public Usuario(String nome, String email, String senhaHash) {
        this.nome = nome;
        this.email = email;
        this.senha = senhaHash;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Devolve o hash apenas para operações internas, como a futura validação
     * de login. O controller nunca deve colocar esse valor em um response.
     */
    public String getSenha() {
        return senha;
    }

    public boolean isAtivo() {
        return ativo;
    }
}
