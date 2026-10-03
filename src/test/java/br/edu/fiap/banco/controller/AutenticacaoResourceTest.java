package br.edu.fiap.banco.controller;

import br.edu.fiap.banco.entity.Usuario;
import br.edu.fiap.banco.security.JwtService;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

// @QuarkusTest sobe o contexto e HTTP local; no Spring, seria um
// @SpringBootTest com cliente HTTP para testar o filtro de segurança.
@QuarkusTest
class AutenticacaoResourceTest {
    @Inject
    JwtService tokens;
    @Inject
    JWTParser parser;

    @Test
    void semTokenRotasProtegidasRetornam401() {
        given().when().get("/api/auth/me").then().statusCode(401);
        given().contentType(ContentType.JSON)
                .body("{\"nome\":\"Teste\",\"cpf\":\"12345678901\",\"email\":\"teste@example.test\"}")
                .when().post("/api/pessoas").then().statusCode(401);
    }

    @Test
    void tokenValidoAcessaMeESignaturaAdulteradaFalha() {
        var usuario = new Usuario("Pessoa Exemplo", "usuario@example.test", "hash-ficticio");
        String jwt = tokens.emitir(usuario).accessToken();
        org.junit.jupiter.api.Assertions.assertEquals(
                "HS256", ConfigProvider.getConfig().getValue("smallrye.jwt.verify.algorithm", String.class));
        try {
            parser.verify(jwt, "chave-ficticia-de-teste-com-mais-de-32-bytes-2026");
        } catch (Exception ex) {
            throw new AssertionError("O token deve validar diretamente com o segredo de teste.", ex);
        }
        try {
            parser.parse(jwt);
        } catch (Exception ex) {
            throw new AssertionError("A configuração de verificação deve aceitar o token.", ex);
        }
        given().auth().oauth2(jwt).when().get("/api/auth/me").then()
                .statusCode(200)
                .body("email", equalTo("usuario@example.test"))
                .body("nome", equalTo("Pessoa Exemplo"));

        String adulterado = jwt.substring(0, jwt.lastIndexOf('.') + 1) + "assinatura-invalida";
        given().auth().oauth2(adulterado).when().get("/api/auth/me").then().statusCode(401);
    }
}
