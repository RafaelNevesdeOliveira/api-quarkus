package br.edu.fiap.banco.controller;

import br.edu.fiap.banco.dto.LoginRequest;
import br.edu.fiap.banco.dto.TokenResponse;
import br.edu.fiap.banco.dto.UsuarioAutenticadoResponse;
import br.edu.fiap.banco.service.AutenticacaoService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

// @Path e @POST/@GET mapeiam rotas como @RequestMapping,
// @PostMapping e @GetMapping no Spring MVC.
@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AutenticacaoResource {
    private final AutenticacaoService service;

    // CDI injeta as claims do token verificado; no Spring Security,
    // Authentication/JwtAuthenticationToken daria acesso ao principal.
    @Inject
    JsonWebToken jwt;

    @Inject
    public AutenticacaoResource(AutenticacaoService service) {
        this.service = service;
    }

    // Login é público. @NotNull/@Valid validam o DTO, assim como
    // @Valid @RequestBody em um controller Spring.
    @POST
    @Path("/login")
    public TokenResponse login(@NotNull @Valid LoginRequest pedido) {
        return service.login(pedido);
    }

    /** Exercício HTTP que comprova a verificação do token sem consultar o banco. */
    @GET
    @Path("/me")
    // @Authenticated exige Bearer válido; Spring Security usaria
    // authenticated() no SecurityFilterChain para esta rota.
    @Authenticated
    public UsuarioAutenticadoResponse me() {
        Number id = jwt.getClaim("usuarioId");
        return new UsuarioAutenticadoResponse(
                jwt.getSubject(), jwt.getClaim("nome"), id == null ? null : id.longValue());
    }
}
