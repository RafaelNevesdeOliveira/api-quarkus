package br.edu.fiap.banco.repository;

import br.edu.fiap.banco.entity.Pessoa;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * A anotação @ApplicationScoped registra este bean CDI como @Repository faria no Spring.
 * PanacheRepository oferece persist/count/findById; no Spring Data JPA,
 * uma interface JpaRepository fornece operações semelhantes.
 * A interface PessoaRepository mantém o service desacoplado da escolha.
 */
@ApplicationScoped
public class PessoaRepositoryJpa implements PessoaRepository, PanacheRepository<Pessoa> {
    @Override
    public boolean existePorCpf(String cpf) {
        return count("cpf", cpf) > 0;
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) {
        persistAndFlush(pessoa);
        return pessoa;
    }
}
