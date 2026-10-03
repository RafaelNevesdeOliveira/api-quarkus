package br.edu.fiap.banco.exception;

import java.math.BigDecimal;

/** Erro 409 que impede o saque de um valor maior que o saldo. */
public class SaldoInsuficienteException extends ConflitoDeNegocioException {
    public SaldoInsuficienteException(Long id, BigDecimal saldo, BigDecimal valor) {
        super("Saldo insuficiente na conta " + id + ". Saldo: " + saldo + ", saque: " + valor);
    }
}
