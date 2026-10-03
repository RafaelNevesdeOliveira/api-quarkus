package br.edu.fiap.banco.security;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * BCrypt compatível com os hashes de 60 caracteres da tabela usuarios.
 * A anotação @ApplicationScoped cria o bean CDI, semelhante a um @Component Spring.
 * BcryptUtil cria/confere hash como PasswordEncoder do Spring Security.
 */
@ApplicationScoped
public class SenhaBCrypt {
    private final int custo;

    // @ConfigProperty lê a configuração Quarkus; no Spring, @Value
    // ou @ConfigurationProperties teria função semelhante.
    public SenhaBCrypt(@ConfigProperty(name = "security.password.bcrypt-cost") int custo) {
        this.custo = custo;
    }

    public String criarHash(String senha) {
        return BcryptUtil.bcryptHash(senha, custo);
    }

    public boolean confere(String senha, String hash) {
        return BcryptUtil.matches(senha, hash);
    }
}
