package br.edu.fiap.banco.exception;

/** Representa uma operação válida em HTTP, mas proibida pelo estado do negócio. */
public class ConflitoDeNegocioException extends RuntimeException {
    public ConflitoDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
