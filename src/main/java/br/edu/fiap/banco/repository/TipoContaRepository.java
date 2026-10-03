package br.edu.fiap.banco.repository;

import br.edu.fiap.banco.entity.TipoConta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// CDI @ApplicationScoped registra o repository; no Spring, a interface
// JpaRepository normalmente recebe comportamento similar de persistência.
@ApplicationScoped
public class TipoContaRepository implements PanacheRepository<TipoConta> {
}
