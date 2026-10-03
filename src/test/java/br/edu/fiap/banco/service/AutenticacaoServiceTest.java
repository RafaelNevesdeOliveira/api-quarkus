package br.edu.fiap.banco.service;

import br.edu.fiap.banco.dto.LoginRequest;
import br.edu.fiap.banco.entity.Usuario;
import br.edu.fiap.banco.repository.UsuarioRepository;
import br.edu.fiap.banco.security.JwtService;
import br.edu.fiap.banco.security.SenhaBCrypt;
import jakarta.ws.rs.NotAuthorizedException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutenticacaoServiceTest {
    private final SenhaBCrypt senhas = new SenhaBCrypt(4);
    private final JwtService tokens = new JwtService(
            "chave-ficticia-de-teste-com-mais-de-32-bytes-2026",
            "agencia-bancaria-api", 900);

    @Test
    void loginValidoEmiteBearerSemSenhaNoResponse() {
        Usuario usuario = new Usuario("Pessoa Exemplo", "usuario@example.test",
                senhas.criarHash("SenhaFicticia123!"));
        var service = new AutenticacaoService(new RepositorioFalso(usuario), senhas, tokens);
        var resposta = service.login(new LoginRequest("usuario@example.test", "SenhaFicticia123!"));
        assertEquals("Bearer", resposta.tokenType());
        assertEquals(900, resposta.expiresIn());
        assertEquals(3, resposta.accessToken().split("\\.").length);
    }

    @Test
    void loginOcultaSeEmailOuSenhaEstaoErrados() {
        Usuario usuario = new Usuario("Pessoa Exemplo", "usuario@example.test",
                senhas.criarHash("SenhaFicticia123!"));
        var service = new AutenticacaoService(new RepositorioFalso(usuario), senhas, tokens);
        assertThrows(NotAuthorizedException.class,
                () -> service.login(new LoginRequest("inexistente@example.test", "SenhaFicticia123!")));
        assertThrows(NotAuthorizedException.class,
                () -> service.login(new LoginRequest("usuario@example.test", "senha-errada")));
    }

    private static final class RepositorioFalso extends UsuarioRepository {
        private final Usuario usuario;

        private RepositorioFalso(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public Optional<Usuario> buscarPorEmail(String email) {
            return usuario.getEmail().equalsIgnoreCase(email)
                    ? Optional.of(usuario) : Optional.empty();
        }
    }
}
