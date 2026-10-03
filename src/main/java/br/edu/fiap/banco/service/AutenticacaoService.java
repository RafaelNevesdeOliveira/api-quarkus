package br.edu.fiap.banco.service;

import br.edu.fiap.banco.dto.LoginRequest;
import br.edu.fiap.banco.dto.TokenResponse;
import br.edu.fiap.banco.entity.Usuario;
import br.edu.fiap.banco.repository.UsuarioRepository;
import br.edu.fiap.banco.security.JwtService;
import br.edu.fiap.banco.security.SenhaBCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;

// @ApplicationScoped registra o serviço CDI, equivalente ao papel de
// @Service no Spring. @Inject abaixo escolhe o construtor.
@ApplicationScoped
public class AutenticacaoService {
    private final UsuarioRepository usuarios;
    private final SenhaBCrypt senhas;
    private final JwtService tokens;

    @Inject
    public AutenticacaoService(UsuarioRepository usuarios, SenhaBCrypt senhas, JwtService tokens) {
        this.usuarios = usuarios;
        this.senhas = senhas;
        this.tokens = tokens;
    }

    // Jakarta @Transactional mantém a leitura JPA dentro de uma transação;
    // em Spring, usaríamos @Transactional do pacote Spring.
    @Transactional
    public TokenResponse login(LoginRequest pedido) {
        Usuario usuario = usuarios.buscarPorEmail(pedido.email())
                .orElseThrow(() -> new NotAuthorizedException("Bearer"));
        if (!usuario.isAtivo() || !senhas.confere(pedido.senha(), usuario.getSenha())) {
            throw new NotAuthorizedException("Bearer");
        }
        return tokens.emitir(usuario);
    }
}
