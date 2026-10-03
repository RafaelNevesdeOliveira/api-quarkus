package br.edu.fiap.banco.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Contrato JSON necessário para abrir uma conta.
 *
 * <p>Os IDs do titular e do tipo permitem que o service localize registros já
 * existentes e construa as duas chaves estrangeiras.</p>
 * As anotações @NotBlank, @Pattern, @NotNull, @DecimalMin e @Positive são Jakarta
 * Bean Validation, reutilizadas da mesma forma em controllers Spring.
 */
public record ContaBancariaRequest(
        @NotBlank(message = "Agência é obrigatória.")
        @Pattern(regexp = "\\d{4}", message = "Agência deve ter 4 números.")
        String agencia,

        @NotBlank(message = "Número é obrigatório.")
        @Pattern(regexp = "\\d{6}-\\d", message = "Número deve seguir o formato 123456-7.")
        String numero,

        @NotNull(message = "Saldo inicial é obrigatório.")
        @DecimalMin(value = "0.00", message = "Saldo inicial não pode ser negativo.")
        BigDecimal saldoInicial,

        @NotNull(message = "Situação ativa é obrigatória.")
        Boolean ativa,

        @NotNull(message = "Titular é obrigatório.")
        @Positive(message = "ID do titular deve ser positivo.")
        Long titularId,

        @NotNull(message = "Tipo da conta é obrigatório.")
        @Positive(message = "ID do tipo de conta deve ser positivo.")
        Long tipoContaId) {
}
