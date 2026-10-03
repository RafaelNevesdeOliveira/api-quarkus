package br.edu.fiap.banco.dto;

import br.edu.fiap.banco.entity.Usuario;

/** A senha e o hash ficam fora do contrato público. */
public record UsuarioResponse(Long id, String nome, String email, boolean ativo) {
    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(),
                usuario.getEmail(), usuario.isAtivo());
    }
}
