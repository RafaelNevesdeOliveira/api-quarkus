package br.edu.fiap.banco.repository;

import br.edu.fiap.banco.entity.Pessoa;
import br.edu.fiap.banco.exception.CpfJaCadastradoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * SQL manual para mostrar o trabalho que JPA fará no Dia 3.
 * CDI @ApplicationScoped registra o repository como bean, papel semelhante
 * ao @Repository do Spring. DataSource é injetado nos dois frameworks;
 * aqui usamos PreparedStatement diretamente, enquanto no Spring
 * JdbcTemplate costuma reduzir este código repetitivo.
 */
@ApplicationScoped
public class PessoaRepositoryJdbc implements PessoaRepository {
    private final DataSource dataSource;

    // @Inject pede ao CDI o DataSource configurado pelo Quarkus.
    // A versão Spring poderia recebê-lo no construtor com @Autowired.
    @Inject
    public PessoaRepositoryJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean existePorCpf(String cpf) {
        String sql = "SELECT 1 FROM public.pessoas WHERE cpf = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cpf);
            try (ResultSet resultado = statement.executeQuery()) {
                return resultado.next();
            }
        } catch (SQLException erro) {
            throw new IllegalStateException("Falha ao consultar pessoas.", erro);
        }
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) {
        String sql = "INSERT INTO public.pessoas (nome, cpf, email) VALUES (?, ?, ?) RETURNING id";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pessoa.nome());
            statement.setString(2, pessoa.cpf());
            statement.setString(3, pessoa.email());
            try (ResultSet resultado = statement.executeQuery()) {
                if (!resultado.next()) {
                    throw new IllegalStateException("O INSERT não devolveu o ID da pessoa.");
                }
                return pessoa.comId(resultado.getLong("id"));
            }
        } catch (SQLException erro) {
            // A restrição UNIQUE do PostgreSQL também protege duas chamadas concorrentes.
            if ("23505".equals(erro.getSQLState())) {
                throw new CpfJaCadastradoException();
            }
            throw new IllegalStateException("Falha ao salvar pessoa.", erro);
        }
    }
}
