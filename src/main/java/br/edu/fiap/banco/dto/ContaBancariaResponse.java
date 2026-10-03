package br.edu.fiap.banco.dto;

import br.edu.fiap.banco.entity.ContaBancaria;

import java.math.BigDecimal;

/**
 * Contrato JSON que apresenta a conta e seus dois relacionamentos.
 *
 * <p>A estrutura plana evita expor proxies do Hibernate ou produzir ciclos de
 * serialização entre entidades relacionadas.</p>
 */
public record ContaBancariaResponse(
        Long id,
        String agencia,
        String numero,
        BigDecimal saldo,
        boolean ativa,
        Long titularId,
        String titularNome,
        Long tipoContaId,
        String tipoContaNome) {

    /**
     * Monta uma visão plana para evitar ciclos e detalhes internos do Hibernate.
     */
    public static ContaBancariaResponse de(ContaBancaria conta) {
        return new ContaBancariaResponse(
                conta.getId(), conta.getAgencia(), conta.getNumero(), conta.getSaldo(), conta.isAtiva(),
                conta.getTitular().getId(), conta.getTitular().getNome(),
                conta.getTipoConta().getId(), conta.getTipoConta().getNome());
    }
}
