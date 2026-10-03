package br.edu.fiap.banco.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

// @Provider registra este ExceptionMapper no Jakarta REST; no Spring,
// @ControllerAdvice + @ExceptionHandler converteriam a exceção em HTTP.
@Provider
public class ConflitoDeNegocioMapper implements ExceptionMapper<ConflitoDeNegocioException> {
    @Override
    public Response toResponse(ConflitoDeNegocioException erro) {
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("mensagem", erro.getMessage())).build();
    }
}
