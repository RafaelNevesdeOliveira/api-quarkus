# Validação do gabarito Dia 5

Executado em 2 de outubro de 2026 com Quarkus 3.40.1, Temurin JDK 17.0.18
e Maven 3.9.9: `mvn test package` concluiu o pacote JVM. Foram 10 testes,
0 falhas e 0 erros. O `@QuarkusTest` iniciou HTTP local temporário e
comprovou 401 sem token, 200 com token HS256 válido e 401 com assinatura
adulterada. O teste de serviço confirmou senha BCrypt válida e rejeição
indistinta de e-mail ou senha incorretos. Os 6 testes dos Dias 3–4 seguiram
passando.

`src/test/resources/application.properties` contém somente valores
fictícios e inicializa Hibernate ORM offline. Não houve conexão com
PostgreSQL, container ou alteração do esquema. Cadastro e login completos
contra banco real não foram executados.
