package br.edu.fiap.banco.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Contrato JSON compartilhado pelas rotas de depósito e saque.
 *
 * <p>A validação de entrada rejeita ausência, zero e valor negativo antes que a
 * requisição alcance a regra de negócio da conta.</p>
 * As anotações @NotNull e @DecimalMin também seriam usadas no DTO Spring.
 */
public record MovimentacaoRequest(
        @NotNull(message = "Valor é obrigatório.")
        @DecimalMin(value = "0.01", message = "Valor deve ser pelo menos 0,01.")
        BigDecimal valor) {
}
