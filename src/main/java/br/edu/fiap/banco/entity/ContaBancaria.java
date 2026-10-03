package br.edu.fiap.banco.entity;

import br.edu.fiap.banco.exception.ContaInativaException;
import br.edu.fiap.banco.exception.SaldoInsuficienteException;
import br.edu.fiap.banco.exception.ValorMovimentacaoInvalidoException;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Entidade central do desafio. Guarda o saldo e as referências ao titular e ao
 * tipo da conta. As regras que protegem o próprio estado ficam nesta classe.
 * As anotações @Entity/@Table são as mesmas Jakarta JPA usadas com Spring Data.
 */
@Entity
@Table(name = "contas_bancarias")
public class ContaBancaria {

    // @Id e @GeneratedValue(IDENTITY) mapeiam a chave gerada no banco,
    // sem diferença de anotação em relação ao Spring Data JPA.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String agencia;

    @Column(nullable = false, length = 8)
    private String numero;

    // @Column preserva NUMERIC(15,2); no Spring JPA o mapeamento é idêntico.
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false)
    private boolean ativa;

    // @ManyToOne(LAZY) evita carregar a pessoa antes de ser necessária;
    // @JoinColumn nomeia a FK. Ambas são Jakarta JPA também no Spring.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "titular_id", nullable = false)
    private Pessoa titular;

    // O segundo relacionamento segue a mesma regra para o tipo de conta.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_conta_id", nullable = false)
    private TipoConta tipoConta;

    protected ContaBancaria() {
        // Uso exclusivo do JPA.
    }

    public ContaBancaria(String agencia, String numero, BigDecimal saldoInicial,
                         boolean ativa, Pessoa titular, TipoConta tipoConta) {
        if (saldoInicial == null || saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        this.agencia = agencia;
        this.numero = numero;
        this.saldo = saldoInicial;
        this.ativa = ativa;
        this.titular = titular;
        this.tipoConta = tipoConta;
    }

    /** Soma um valor positivo ao saldo da conta ativa. */
    public void depositar(BigDecimal valor) {
        validarContaAtiva();
        validarValor(valor);
        saldo = saldo.add(valor);
    }

    /** Subtrai um valor positivo sem permitir que o saldo fique negativo. */
    public void sacar(BigDecimal valor) {
        validarContaAtiva();
        validarValor(valor);
        if (saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException(id, saldo, valor);
        }
        saldo = saldo.subtract(valor);
    }

    private void validarContaAtiva() {
        if (!ativa) {
            throw new ContaInativaException(id);
        }
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorMovimentacaoInvalidoException();
        }
    }

    public Long getId() { return id; }
    public String getAgencia() { return agencia; }
    public String getNumero() { return numero; }
    public BigDecimal getSaldo() { return saldo; }
    public boolean isAtiva() { return ativa; }
    public Pessoa getTitular() { return titular; }
    public TipoConta getTipoConta() { return tipoConta; }
}
