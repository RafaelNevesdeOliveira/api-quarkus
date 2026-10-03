# Validação do Dia 2

Quarkus 3.40.1, JDK 17.0.18, Maven 3.9.9. `mvn test package` passou com
2 testes unitários, 0 falhas e 0 erros. O `quarkus-run.jar` JVM foi gerado.
As variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD` no build apontavam para valores
fictícios; nenhuma conexão foi aberta. Não foram feitos testes de integração
PostgreSQL nem testes HTTP neste estágio.
