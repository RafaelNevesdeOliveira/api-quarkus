package br.edu.fiap.banco.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Mesmo contrato de cadastro do Spring. @NotBlank, @Email e @Size são
 * restrições Jakarta Bean Validation usadas nos dois frameworks.
 * @Size conta caracteres; o service ainda verifica o limite de 72 bytes
 * UTF-8 antes de chamar BCrypt.
 */
public record UsuarioRequest(
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Email @Size(max = 160) String email,
        @NotBlank @Size(min = 8, max = 72) String senha) {
}
