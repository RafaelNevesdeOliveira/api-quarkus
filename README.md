# Agência Bancária em Quarkus — Dia 5, JWT e testes

Esta cópia incremental do Dia 4 mantém o esquema PostgreSQL do projeto
Spring e acrescenta login JWT. `POST /api/usuarios` (cadastro) e
`POST /api/auth/login` são públicos. `/api/pessoas`, `/api/contas`,
as leituras de `/api/usuarios` e `GET /api/auth/me` exigem
`Authorization: Bearer <token>`.

## Fluxo para explicar em aula

1. `AutenticacaoResource` valida o JSON `LoginRequest(email, senha)`.
2. `AutenticacaoService` busca o usuário por e-mail, confirma `ativo` e
   compara a senha com o hash BCrypt. E-mail inexistente e senha incorreta
   recebem o mesmo 401.
3. `JwtService` assina com HS256 por 15 minutos. O token contém `sub`
   (e-mail), `iss`, `iat`, `exp`, `nome` e `usuarioId`; não contém senha
   nem hash. Não há papel de administrador inventado neste exercício.
4. O filtro SmallRye JWT verifica assinatura, emissor e validade antes dos
   recursos anotados com `@Authenticated`. `GET /api/auth/me` devolve
   as claims de identidade sem consultar o banco.

No Spring original, filtro e `SecurityFilterChain` cumprem a função do filtro
JWT do Quarkus; `@Authenticated` protege a rota. `@PermitAll` mantém o
cadastro público. O token é portador; não o coloque em logs ou repositórios.

## Configuração da chave e do banco

`application.properties` exige `DB_URL`, `DB_USER`, `DB_PASSWORD`,
`JWT_SECRET` e `JWT_VERIFY_JWK`. O segredo precisa ter ao menos 32 bytes
UTF-8. O verificador SmallRye lê a mesma chave em JWK (`kty=oct`,
`alg=HS256`, `k` em Base64URL). Exemplo Unix em instância didática
isolada:

```sh
export DB_URL='jdbc:postgresql://localhost:5432/agencia_bancaria_didatica'
export DB_USER='didatico'
export DB_PASSWORD='<senha-local-da-instancia-didatica>'
export JWT_SECRET="$(openssl rand -hex 32)"
export JWT_VERIFY_JWK="$(python3 -c 'import base64,json,os; key=os.environ["JWT_SECRET"].encode("utf-8"); print(json.dumps({"kty":"oct","alg":"HS256","k":base64.urlsafe_b64encode(key).decode().rstrip("=")},separators=(",",":")))' )"
mvn quarkus:dev
```

Use `sql/01_criar_tabelas.sql` na instância didática antes das rotas JPA.
A carga `sql/02_carga_dados_sintetica.sql` é opcional. Não aplique esses
scripts sobre o banco original. Proteja as duas variáveis JWT: ambas contêm
a mesma chave. `smallrye.jwt.sign.key` recebe o JWK para impedir que Quarkus
gere automaticamente um par RSA no modo dev/test; `JwtService` assina com
`JWT_SECRET`.

`JWT_ISSUER` pode substituir `agencia-bancaria-api` e
`JWT_EXPIRATION_SECONDS` pode substituir 900. O issuer deve coincidir no
emissor e verificador. Para o fast-jar JVM: `mvn test package` e
`java -jar target/quarkus-app/quarkus-run.jar`, preservando toda a pasta
`target/quarkus-app`.

## Limites e verificação

O laboratório Argon2id de quinta segue isolado; o login usa BCrypt porque
`usuarios.senha` continua `VARCHAR(60)`. Tokens já emitidos não consultam
novamente `ativo`; o exercício usa expiração curta, sem renovação, revogação
ou autorização por papel.

O build JVM e 10 testes passaram: 4 de saldo, 2 de senha, 2 do serviço de
login e 2 HTTP de proteção/assinatura JWT. Os testes HTTP usaram chave
fictícia, ORM offline e não acessaram PostgreSQL. Cadastro e login completos
com banco real ainda exigem validação em instância didática separada.

Fontes: https://quarkus.io/guides/security-jwt/ ;
https://quarkus.io/guides/security-jwt-build/ .
