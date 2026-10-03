package br.edu.fiap.banco.exception;

/** Erro 409 que impede movimentações em uma conta inativa. */
public class ContaInativaException extends ConflitoDeNegocioException {
    public ContaInativaException(Long id) {
        super("A conta " + id + " está inativa e não pode ser movimentada.");
    }
}
