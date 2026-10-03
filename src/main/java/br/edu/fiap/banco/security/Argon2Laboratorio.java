package br.edu.fiap.banco.security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

/** Laboratório JVM puro. Não grava hashes Argon2 na tabela usuarios. */
public class Argon2Laboratorio {
    public static final int MEMORIA_KIB = 19_456;
    public static final int ITERACOES = 2;
    public static final int PARALELISMO = 1;
    private final SecureRandom aleatorio = new SecureRandom();

    public record Resultado(String salBase64, String hashBase64,
                            int memoriaKiB, int iteracoes, int paralelismo) {
    }

    public Resultado criarHash(char[] senha) {
        byte[] sal = new byte[16];
        aleatorio.nextBytes(sal);
        byte[] hash = calcular(senha, sal);
        return new Resultado(Base64.getEncoder().encodeToString(sal),
                Base64.getEncoder().encodeToString(hash),
                MEMORIA_KIB, ITERACOES, PARALELISMO);
    }

    public boolean confere(char[] tentativa, Resultado esperado) {
        byte[] sal = Base64.getDecoder().decode(esperado.salBase64());
        byte[] hashEsperado = Base64.getDecoder().decode(esperado.hashBase64());
        byte[] hashTentativa = calcular(tentativa, sal);
        return MessageDigest.isEqual(hashEsperado, hashTentativa);
    }

    private byte[] calcular(char[] senha, byte[] sal) {
        Argon2Parameters parametros = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withSalt(sal)
                .withMemoryAsKB(MEMORIA_KIB)
                .withIterations(ITERACOES)
                .withParallelism(PARALELISMO)
                .build();
        byte[] saida = new byte[32];
        Argon2BytesGenerator gerador = new Argon2BytesGenerator();
        gerador.init(parametros);
        gerador.generateBytes(senha, saida);
        return saida;
    }
}
