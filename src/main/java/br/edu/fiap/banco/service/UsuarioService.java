package br.edu.fiap.banco.service;

import br.edu.fiap.banco.dto.UsuarioRequest;
import br.edu.fiap.banco.dto.UsuarioResponse;
import br.edu.fiap.banco.entity.Usuario;
import br.edu.fiap.banco.exception.ConflitoDeNegocioException;
import br.edu.fiap.banco.repository.UsuarioRepository;
import br.edu.fiap.banco.security.SenhaBCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.BadRequestException;
import java.nio.charset.StandardCharsets;
import java.util.List;

// @ApplicationScoped é o bean CDI que exerce o papel de @Service no Spring.
@ApplicationScoped
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final SenhaBCrypt senhas;

    // @Inject faz injeção pelo construtor, equivalente ao padrão do Spring.
    @Inject
    public UsuarioService(UsuarioRepository usuarios, SenhaBCrypt senhas) {
        this.usuarios = usuarios;
        this.senhas = senhas;
    }

    // Jakarta @Transactional delimita a gravação JPA; Spring tem sua
    // própria @Transactional com o mesmo propósito.
    @Transactional
    public UsuarioResponse cadastrar(UsuarioRequest pedido) {
        if (pedido.senha().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new BadRequestException("Senha excede o limite de 72 bytes do BCrypt.");
        }
        if (usuarios.existePorEmail(pedido.email())) {
            throw new ConflitoDeNegocioException("E-mail já cadastrado.");
        }
        String hash = senhas.criarHash(pedido.senha());
        Usuario usuario = new Usuario(pedido.nome(), pedido.email(), hash);
        usuarios.persistAndFlush(usuario);
        return UsuarioResponse.de(usuario);
    }

    @Transactional
    public List<UsuarioResponse> listar() {
        return usuarios.listAll().stream().map(UsuarioResponse::de).toList();
    }

    @Transactional
    public UsuarioResponse buscar(Long id) {
        Usuario usuario = usuarios.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        return UsuarioResponse.de(usuario);
    }
}
