package br.edu.fiap.banco.controller;

import br.edu.fiap.banco.dto.UsuarioRequest;
import br.edu.fiap.banco.dto.UsuarioResponse;
import br.edu.fiap.banco.service.UsuarioService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

// @Path e @POST/@GET expõem as rotas Jakarta REST, equivalentes a
// @RequestMapping, @PostMapping e @GetMapping no Spring MVC.
// @Consumes/@Produces declaram o JSON como consumes/produces no Spring.
@Path("/api/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// @Authenticated protege as leituras; Spring Security usaria uma regra
// authenticated() para estas rotas no SecurityFilterChain.
@Authenticated
public class UsuarioResource {
    private final UsuarioService service;

    // CDI @Inject injeta o service pelo construtor; Spring faz o mesmo
    // com a injeção por construtor de um @Service.
    @Inject
    public UsuarioResource(UsuarioService service) {
        this.service = service;
    }

    // @NotNull/@Valid ativam Jakarta Bean Validation no request, assim
    // como @Valid @RequestBody em um controller Spring.
    // @PermitAll libera o cadastro mesmo com a classe protegida; no
    // Spring Security, requestMatchers(POST, "/api/usuarios").permitAll().
    @POST
    @PermitAll
    public Response cadastrar(@NotNull @Valid UsuarioRequest pedido) {
        UsuarioResponse usuario = service.cadastrar(pedido);
        return Response.created(URI.create("/api/usuarios/" + usuario.id()))
                .entity(usuario).build();
    }

    @GET
    public List<UsuarioResponse> listar() {
        return service.listar();
    }

    // @PathParam lê {id} da URL, papel de @PathVariable no Spring.
    @GET @Path("/{id}")
    public UsuarioResponse buscar(@PathParam("id") Long id) {
        return service.buscar(id);
    }
}
