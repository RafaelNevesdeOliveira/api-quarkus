package br.edu.fiap.banco.controller;

import br.edu.fiap.banco.dto.ContaBancariaRequest;
import br.edu.fiap.banco.dto.ContaBancariaResponse;
import br.edu.fiap.banco.dto.MovimentacaoRequest;
import br.edu.fiap.banco.service.ContaBancariaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

// @Path define a URL base, como @RequestMapping no Spring MVC.
// @Consumes/@Produces declaram JSON, como consumes/produces no Spring.
@Path("/api/contas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContaBancariaResource {
    private final ContaBancariaService service;

    // CDI @Inject resolve o service; Spring injeta pelo construtor.
    @Inject
    public ContaBancariaResource(ContaBancariaService service) {
        this.service = service;
    }

    // @POST equivale a @PostMapping; @NotNull/@Valid validam o corpo,
    // como @Valid @RequestBody numa API Spring.
    @POST
    public Response abrir(@NotNull @Valid ContaBancariaRequest pedido) {
        ContaBancariaResponse conta = service.abrir(pedido);
        return Response.created(URI.create("/api/contas/" + conta.id())).entity(conta).build();
    }

    // @GET + @Path equivale a @GetMapping("/{id}");
    // @PathParam corresponde a @PathVariable no Spring.
    @GET @Path("/{id}")
    public ContaBancariaResponse buscar(@PathParam("id") Long id) {
        return service.buscar(id);
    }

    @GET @Path("/pessoa/{pessoaId}")
    public List<ContaBancariaResponse> listarPorPessoa(@PathParam("pessoaId") Long pessoaId) {
        return service.listarPorPessoa(pessoaId);
    }

    // @PATCH + @Path equivale a @PatchMapping no Spring.
    @PATCH @Path("/{id}/depositos")
    public ContaBancariaResponse depositar(@PathParam("id") Long id,
                                            @NotNull @Valid MovimentacaoRequest pedido) {
        return service.depositar(id, pedido);
    }

    @PATCH @Path("/{id}/saques")
    public ContaBancariaResponse sacar(@PathParam("id") Long id,
                                       @NotNull @Valid MovimentacaoRequest pedido) {
        return service.sacar(id, pedido);
    }
}
