# Agência Bancária em Quarkus — Dia 5, JWT e testes

## Aula 1 — ambiente e primeiro projeto Quarkus

### 1. Conferir Java e Maven

Use **Temurin JDK 25.0.2**, **Maven 3.9.9** e **Quarkus 3.40.1**. O Maven
precisa usar o mesmo JDK que o terminal e a IDE. O POM compila com
`maven.compiler.release=25`: o número 25 é o nível do bytecode Java, enquanto
25.0.2 é a atualização do JDK instalada na máquina.

No macOS, abra um terminal novo e execute:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 25.0.2)
export PATH="$JAVA_HOME/bin:$PATH"
java -version
javac -version
mvn -version
```

No Windows PowerShell, substitua o caminho de exemplo pela pasta real do JDK:

```powershell
$env:JAVA_HOME = 'C:\caminho\real\jdk-25.0.2'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version
javac -version
mvn -version
```

`java` e `javac` devem indicar **25.0.2**; `mvn -version` deve mostrar Maven e
Java **25.0.2**. Se mostrar outro Java, corrija `JAVA_HOME` e reabra o terminal
da IDE. Para instalação, use os sites oficiais do
[Temurin](https://adoptium.net/temurin/releases/?version=25) e do
[Maven](https://maven.apache.org/install.html).

### 2. Escolher o ponto de partida

Para fazer o desafio, extraia `DESAFIO_INICIO_CODIGO.zip` da pasta `AULA_01` e
abra `agencia-bancaria-quarkus` na IDE. Esse ZIP já contém o projeto inicial;
pule o comando de geração abaixo. Se preferir o GitHub:

```sh
git clone https://github.com/RafaelNevesdeOliveira/api-quarkus.git
cd api-quarkus
git switch aluno-inicio
```

Para **criar outro projeto do zero**, execute o comando em uma pasta de trabalho
que ainda não contenha `agencia-bancaria-quarkus`:

```sh
mvn io.quarkus.platform:quarkus-maven-plugin:3.40.1:create \
  -DprojectGroupId=br.edu.fiap \
  -DprojectArtifactId=agencia-bancaria-quarkus \
  -DprojectVersion=1.0.0-SNAPSHOT \
  -Dextensions=rest-jackson,hibernate-validator \
  -DjavaVersion=25 \
  -DnoCode
cd agencia-bancaria-quarkus
```

No Windows PowerShell, use uma linha; as opções `-D` ficam entre aspas:

```powershell
mvn io.quarkus.platform:quarkus-maven-plugin:3.40.1:create "-DprojectGroupId=br.edu.fiap" "-DprojectArtifactId=agencia-bancaria-quarkus" "-DprojectVersion=1.0.0-SNAPSHOT" "-Dextensions=rest-jackson,hibernate-validator" "-DjavaVersion=25" "-DnoCode"
cd agencia-bancaria-quarkus
```

`rest-jackson` prepara REST e JSON; `hibernate-validator` prepara validação;
`-DnoCode` deixa a API bancária para o desafio. Confira no `pom.xml` a
plataforma `3.40.1` e `<maven.compiler.release>25</maven.compiler.release>`.
O gerador inclui exemplos em `src/main/docker` e `.dockerignore`: apague esses
arquivos na IDE. Eles não fazem parte das aulas.

### 3. Iniciar e construir na JVM

No projeto recém-gerado, coloque esta linha em
`src/main/resources/application.properties`. No ZIP inicial, confira o arquivo
e acrescente a linha se ele estiver vazio:

```properties
quarkus.http.port=8081
```

Na pasta que contém o `pom.xml`, execute:

```sh
mvn quarkus:dev
```

O log deve indicar a aplicação JVM em `http://localhost:8081`. Pare com
`Ctrl+C`. No projeto recém-gerado ou no ZIP inicial, `/api/pessoas` ainda
responde **404**; o recurso será criado durante o desafio. No gabarito ele já
está implementado. Quando o cadastro estiver pronto, execute:

```sh
mvn test package
java -jar target/quarkus-app/quarkus-run.jar
```

O pacote JVM usa toda a pasta `target/quarkus-app`, não apenas o JAR. Na aula 1
os dados ficam em memória; não é preciso iniciar PostgreSQL. O curso não usa
Docker nem compilação nativa.

| No projeto Spring | Nesta aula em Quarkus |
|---|---|
| `mvn spring-boot:run` | `mvn quarkus:dev` |
| `server.port=8081` | `quarkus.http.port=8081` |
| `@RestController` e `@PostMapping` | `@Path` e `@POST` no `PessoaResource` |

Consulte o [guia oficial de início do Quarkus](https://quarkus.io/guides/getting-started/)
para os comandos de desenvolvimento e empacotamento JVM.

## Aula 5 — JWT e testes

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
