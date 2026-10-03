# Agência Bancária em Quarkus — Dia 4, senhas

Cópia incremental do Dia 3. O domínio `Usuario` foi trazido do projeto Spring,
separado de `Pessoa`: titular bancário e identidade de acesso têm papéis
diferentes. `POST /api/usuarios` recebe a senha apenas no request. O service
gera BCrypt antes de persistir e responde com `UsuarioResponse`, sem senha nem
hash. Nesta etapa as rotas ainda não exigem login; JWT entra no Dia 5.

## BCrypt da aplicação

`SenhaBCrypt` usa `BcryptUtil` do Quarkus com custo 12 configurável por
`BCRYPT_COST`. O hash MCF contém salt aleatório e custa 60 caracteres,
compatível com `usuarios.senha VARCHAR(60)` do **mesmo** esquema SQL bancário.
No Spring original, `PasswordEncoder.encode` e `matches` exercem o mesmo papel.
O service também recusa senha acima de 72 **bytes** UTF-8, limite relevante do
BCrypt; o DTO conserva o limite original de 8 a 72 caracteres.

## Laboratório Argon2id

`Argon2Laboratorio` usa Bouncy Castle `bcprov-jdk18on:1.86` na JVM, com salt
aleatório de 16 bytes, memória de 19 MiB, duas iterações e paralelismo 1.
É um exercício de gerar e conferir hash, sem endpoint e sem persistência.
O `Resultado` do laboratório guarda salt/hash e parâmetros separadamente; ele
não é uma implementação de formato PHC para produção.

Argon2 **não está integrado ao cadastro ou login**. Trocar o algoritmo exigiria
no mínimo ampliar a coluna `usuarios.senha` (o hash codificado costuma exceder
60 caracteres), registrar qual algoritmo foi usado, validar o hash antigo na
entrada seguinte e gerar um novo hash Argon2 após autenticação bem-sucedida.
Também seria preciso planejar rollback, custos de memória e testes de migração.
Nenhum DDL foi alterado neste estágio.

## Verificação realizada

Quarkus 3.40.1, JDK 17, Maven 3.9.9: `mvn test package` passou com **6 testes
unitários**. Dois testam BCrypt/Argon2 com dados fictícios; quatro cobrem
regras de saldo. O build JVM compila cadastro, DTO e JPA. PostgreSQL, rotas
HTTP de usuário e gravação do hash não foram executados. Nenhum container foi
iniciado.

Fontes técnicas: https://quarkus.io/guides/security-jpa/ ;
https://www.bouncycastle.org/download/bouncy-castle-java/ ;
https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html .
