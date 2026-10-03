package br.edu.fiap.banco.exception;

/** Erro 400 usado quando depósito ou saque não é maior que zero. */
public class ValorMovimentacaoInvalidoException extends IllegalArgumentException {
    public ValorMovimentacaoInvalidoException() {
        super("O valor da movimentação deve ser maior que zero.");
    }
}
