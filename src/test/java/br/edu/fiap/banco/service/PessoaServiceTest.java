package br.edu.fiap.banco.service;

import br.edu.fiap.banco.dto.PessoaRequest;
import br.edu.fiap.banco.entity.Pessoa;
import br.edu.fiap.banco.exception.CpfJaCadastradoException;
import br.edu.fiap.banco.repository.PessoaRepository;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Testes de regra sem abrir conexão PostgreSQL. */
class PessoaServiceTest {
    private final PessoaService service = new PessoaService(new RepositorioFalso());

    @Test
    void cadastraPessoa() {
        var resposta = service.cadastrar(new PessoaRequest("Ana", "11111111111", "ana@example.test"));
        assertEquals(1L, resposta.id());
        assertEquals("Ana", resposta.nome());
    }

    @Test
    void rejeitaCpfRepetido() {
        var pedido = new PessoaRequest("Ana", "22222222222", "ana@example.test");
        service.cadastrar(pedido);
        assertThrows(CpfJaCadastradoException.class, () -> service.cadastrar(pedido));
    }

    static class RepositorioFalso implements PessoaRepository {
        private final Map<String, Pessoa> pessoas = new HashMap<>();
        @Override public boolean existePorCpf(String cpf) { return pessoas.containsKey(cpf); }
        @Override public Pessoa salvar(Pessoa pessoa) {
            Pessoa salva = pessoa.comId((long) pessoas.size() + 1);
            pessoas.put(salva.cpf(), salva);
            return salva;
        }
    }
}
