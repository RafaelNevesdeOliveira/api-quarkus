package br.edu.fiap.banco.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// @NotBlank/@Email são Jakarta Bean Validation em Quarkus e Spring;
// @Valid no resource aciona essas verificações antes da lógica de login.
public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String senha) {
}
