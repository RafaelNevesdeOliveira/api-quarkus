package br.edu.fiap.banco.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SenhasTest {
    @Test
    void bcryptUsaSalEConfereSenha() {
        SenhaBCrypt bcrypt = new SenhaBCrypt(12);
        String um = bcrypt.criarHash("SenhaFicticia123!");
        String dois = bcrypt.criarHash("SenhaFicticia123!");
        assertEquals(60, um.length());
        assertNotEquals(um, dois);
        assertTrue(bcrypt.confere("SenhaFicticia123!", um));
        assertFalse(bcrypt.confere("senha-errada", um));
    }

    @Test
    void argon2idUsaSalParametrosEConfereSenha() {
        Argon2Laboratorio argon2 = new Argon2Laboratorio();
        var um = argon2.criarHash("SenhaFicticia123!".toCharArray());
        var dois = argon2.criarHash("SenhaFicticia123!".toCharArray());
        assertNotEquals(um.salBase64(), dois.salBase64());
        assertNotEquals(um.hashBase64(), dois.hashBase64());
        assertEquals(19_456, um.memoriaKiB());
        assertTrue(argon2.confere("SenhaFicticia123!".toCharArray(), um));
        assertFalse(argon2.confere("senha-errada".toCharArray(), um));
    }
}
