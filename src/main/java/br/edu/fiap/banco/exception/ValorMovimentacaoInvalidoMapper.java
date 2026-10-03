package br.edu.fiap.banco.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

// @Provider registra a tradução da regra de negócio para resposta HTTP;
// no Spring MVC, usaríamos @ControllerAdvice + @ExceptionHandler.
@Provider
public class ValorMovimentacaoInvalidoMapper implements ExceptionMapper<ValorMovimentacaoInvalidoException> {
    @Override
    public Response toResponse(ValorMovimentacaoInvalidoException erro) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("mensagem", erro.getMessage())).build();
    }
}
