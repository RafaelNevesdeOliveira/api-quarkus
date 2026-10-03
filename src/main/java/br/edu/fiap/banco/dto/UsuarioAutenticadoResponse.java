package br.edu.fiap.banco.dto;

public record UsuarioAutenticadoResponse(
        String email,
        String nome,
        Long usuarioId) {
}
