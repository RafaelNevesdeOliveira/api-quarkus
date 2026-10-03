package br.edu.fiap.banco.security;

import br.edu.fiap.banco.dto.TokenResponse;
import br.edu.fiap.banco.entity.Usuario;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Emite tokens HS256 curtos, com apenas dados de identidade necessários.
 * A anotação @ApplicationScoped registra o serviço CDI; no Spring, seria um @Service.
 * O builder SmallRye substitui a emissão feita por um serviço JWT Spring.
 */
@ApplicationScoped
public class JwtService {
    private final String segredo;
    private final String emissor;
    private final long duracaoSegundos;

    // @ConfigProperty lê configuração do Quarkus; no Spring,
    // @Value ou @ConfigurationProperties injeta valores equivalentes.
    public JwtService(
            @ConfigProperty(name = "security.jwt.secret") String segredo,
            @ConfigProperty(name = "security.jwt.issuer") String emissor,
            @ConfigProperty(name = "security.jwt.expiration-seconds") long duracaoSegundos) {
        if (segredo == null || segredo.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("JWT_SECRET deve conter ao menos 32 bytes UTF-8.");
        }
        if (duracaoSegundos <= 0) {
            throw new IllegalArgumentException("A duração do JWT deve ser positiva.");
        }
        this.segredo = segredo;
        this.emissor = emissor;
        this.duracaoSegundos = duracaoSegundos;
    }

    public TokenResponse emitir(Usuario usuario) {
        long agora = Instant.now().getEpochSecond();
        var claims = Jwt.subject(usuario.getEmail())
                .issuer(emissor)
                .issuedAt(agora)
                .expiresAt(agora + duracaoSegundos)
                .claim("nome", usuario.getNome());
        if (usuario.getId() != null) {
            claims.claim("usuarioId", usuario.getId());
        }
        String token = claims.signWithSecret(segredo);
        return new TokenResponse(token, "Bearer", duracaoSegundos, agora + duracaoSegundos);
    }
}
