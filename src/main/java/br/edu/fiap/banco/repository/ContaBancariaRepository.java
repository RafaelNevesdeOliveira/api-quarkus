package br.edu.fiap.banco.repository;

import br.edu.fiap.banco.entity.ContaBancaria;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

// CDI @ApplicationScoped registra o repository; no Spring, usaríamos
// @Repository e geralmente uma interface JpaRepository.
@ApplicationScoped
public class ContaBancariaRepository implements PanacheRepository<ContaBancaria> {
    public boolean existePorAgenciaENumero(String agencia, String numero) {
        return count("agencia = ?1 and numero = ?2", agencia, numero) > 0;
    }

    public List<ContaBancaria> listarPorPessoa(Long pessoaId) {
        return list("titular.id", pessoaId);
    }
}
