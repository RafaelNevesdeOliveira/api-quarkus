package br.edu.fiap.banco.dto;

public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        long expiresAt) {
}
