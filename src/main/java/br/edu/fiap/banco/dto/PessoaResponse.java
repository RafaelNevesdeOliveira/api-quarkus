package br.edu.fiap.banco.dto;

import br.edu.fiap.banco.entity.Pessoa;

/** Representação JSON do titular cadastrado. */
public record PessoaResponse(Long id, String nome, String cpf, String email) {
    public static PessoaResponse de(Pessoa pessoa) {
        return new PessoaResponse(pessoa.getId(), pessoa.getNome(), pessoa.getCpf(), pessoa.getEmail());
    }
}
