package br.edu.fiap.banco.repository;

import br.edu.fiap.banco.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

// CDI @ApplicationScoped registra o repository; Spring costuma usar
// @Repository e uma interface JpaRepository para consultas semelhantes.
@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {
    public boolean existePorEmail(String email) {
        return count("lower(email) = ?1", email.toLowerCase(java.util.Locale.ROOT)) > 0;
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return find("lower(email) = ?1", email.toLowerCase(java.util.Locale.ROOT))
                .firstResultOptional();
    }
}
