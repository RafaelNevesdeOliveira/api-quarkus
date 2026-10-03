# Validação do Dia 4

`mvn test package` com Quarkus 3.40.1/JDK 17.0.18/Maven 3.9.9:
6 testes unitários, 0 falhas, 0 erros; pacote JVM criado. Testes executaram
BCrypt via BcryptUtil e Argon2id via Bouncy Castle 1.86, ambos com senhas
fictícias. Não foi iniciada conexão PostgreSQL, container ou serviço HTTP.
O cadastro JPA de usuário compila, mas ainda não foi testado contra banco.
