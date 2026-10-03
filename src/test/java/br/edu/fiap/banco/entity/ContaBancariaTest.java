package br.edu.fiap.banco.entity;

import br.edu.fiap.banco.exception.SaldoInsuficienteException;
import br.edu.fiap.banco.exception.ContaInativaException;
import br.edu.fiap.banco.exception.ValorMovimentacaoInvalidoException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContaBancariaTest {
    private ContaBancaria conta() {
        return new ContaBancaria("0001", "123456-7", new BigDecimal("100.00"), true,
                new Pessoa("Pessoa Exemplo", "12345678901", "pessoa@example.test"),
                new TipoConta("CORRENTE"));
    }

    @Test
    void depositaESacaSemFicarNegativa() {
        ContaBancaria conta = conta();
        conta.depositar(new BigDecimal("25.00"));
        conta.sacar(new BigDecimal("20.00"));
        assertEquals(new BigDecimal("105.00"), conta.getSaldo());
    }

    @Test
    void recusaSaqueAcimaDoSaldo() {
        ContaBancaria conta = conta();
        assertThrows(SaldoInsuficienteException.class,
                () -> conta.sacar(new BigDecimal("101.00")));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
    }

    @Test
    void recusaMovimentacaoNaoPositiva() {
        assertThrows(ValorMovimentacaoInvalidoException.class,
                () -> conta().depositar(BigDecimal.ZERO));
    }

    @Test
    void recusaContaInativa() {
        ContaBancaria inativa = new ContaBancaria("0001", "123456-7", BigDecimal.ZERO, false,
                new Pessoa("Pessoa Exemplo", "12345678901", "pessoa@example.test"),
                new TipoConta("CORRENTE"));
        assertThrows(ContaInativaException.class,
                () -> inativa.depositar(BigDecimal.ONE));
    }
}
