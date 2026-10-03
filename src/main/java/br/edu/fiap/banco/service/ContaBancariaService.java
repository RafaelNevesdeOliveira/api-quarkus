package br.edu.fiap.banco.service;

import br.edu.fiap.banco.dto.ContaBancariaRequest;
import br.edu.fiap.banco.dto.ContaBancariaResponse;
import br.edu.fiap.banco.dto.MovimentacaoRequest;
import br.edu.fiap.banco.entity.ContaBancaria;
import br.edu.fiap.banco.entity.Pessoa;
import br.edu.fiap.banco.entity.TipoConta;
import br.edu.fiap.banco.exception.ConflitoDeNegocioException;
import br.edu.fiap.banco.repository.ContaBancariaRepository;
import br.edu.fiap.banco.repository.PessoaRepositoryJpa;
import br.edu.fiap.banco.repository.TipoContaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

/**
 * Coordena abertura e movimentação; a entidade guarda as regras do saldo.
 * A anotação @ApplicationScoped é o escopo CDI deste bean, usado aqui no
 * papel de um @Service do Spring.
 */
@ApplicationScoped
public class ContaBancariaService {
    private final ContaBancariaRepository contas;
    private final PessoaRepositoryJpa pessoas;
    private final TipoContaRepository tipos;

    // @Inject usa injeção de construtor CDI, como no service Spring.
    @Inject
    public ContaBancariaService(ContaBancariaRepository contas,
                                PessoaRepositoryJpa pessoas, TipoContaRepository tipos) {
        this.contas = contas;
        this.pessoas = pessoas;
        this.tipos = tipos;
    }

    // Jakarta @Transactional abre uma transação JPA; Spring usa sua
    // @Transactional para a mesma fronteira. O DTO é montado aqui dentro
    // para acessar os relacionamentos LAZY enquanto a sessão está aberta.
    @Transactional
    public ContaBancariaResponse abrir(ContaBancariaRequest pedido) {
        if (contas.existePorAgenciaENumero(pedido.agencia(), pedido.numero())) {
            throw new ConflitoDeNegocioException("Conta já cadastrada para agência e número.");
        }
        Pessoa titular = pessoas.findByIdOptional(pedido.titularId())
                .orElseThrow(() -> new NotFoundException("Pessoa não encontrada."));
        TipoConta tipo = tipos.findByIdOptional(pedido.tipoContaId())
                .orElseThrow(() -> new NotFoundException("Tipo de conta não encontrado."));
        ContaBancaria conta = new ContaBancaria(pedido.agencia(), pedido.numero(),
                pedido.saldoInicial(), pedido.ativa(), titular, tipo);
        contas.persistAndFlush(conta);
        return ContaBancariaResponse.de(conta);
    }

    @Transactional
    public ContaBancariaResponse buscar(Long id) {
        return ContaBancariaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public List<ContaBancariaResponse> listarPorPessoa(Long pessoaId) {
        if (pessoas.findByIdOptional(pessoaId).isEmpty()) {
            throw new NotFoundException("Pessoa não encontrada.");
        }
        return contas.listarPorPessoa(pessoaId).stream().map(ContaBancariaResponse::de).toList();
    }

    @Transactional
    public ContaBancariaResponse depositar(Long id, MovimentacaoRequest pedido) {
        ContaBancaria conta = buscarEntidade(id);
        conta.depositar(pedido.valor());
        return ContaBancariaResponse.de(conta);
    }

    @Transactional
    public ContaBancariaResponse sacar(Long id, MovimentacaoRequest pedido) {
        ContaBancaria conta = buscarEntidade(id);
        conta.sacar(pedido.valor());
        return ContaBancariaResponse.de(conta);
    }

    private ContaBancaria buscarEntidade(Long id) {
        return contas.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada."));
    }
}
