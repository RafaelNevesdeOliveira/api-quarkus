package br.edu.fiap.banco.service;

import br.edu.fiap.banco.dto.PessoaRequest;
import br.edu.fiap.banco.dto.PessoaResponse;
import br.edu.fiap.banco.entity.Pessoa;
import br.edu.fiap.banco.exception.CpfJaCadastradoException;
import br.edu.fiap.banco.repository.PessoaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Caso de uso: cadastrar um titular sem duplicar CPF.
 * A anotação CDI @ApplicationScoped cria um bean compartilhado da aplicação; no Spring,
 * a classe seria tipicamente marcada com @Service.
 */
@ApplicationScoped
public class PessoaService {
    private final PessoaRepository repository;

    // @Inject escolhe o construtor para CDI; corresponde à injeção por
    // construtor que o Spring também faz com @Autowired.
    @Inject
    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    // Jakarta @Transactional mantém o INSERT JPA em uma transação;
    // Spring usa @Transactional do pacote org.springframework.transaction.
    @Transactional
    public PessoaResponse cadastrar(PessoaRequest request) {
        if (repository.existePorCpf(request.cpf())) {
            throw new CpfJaCadastradoException();
        }
        Pessoa pessoa = new Pessoa(request.nome(), request.cpf(), request.email());
        return PessoaResponse.de(repository.salvar(pessoa));
    }
}
