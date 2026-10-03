# Validação de dia-05 — 3 de outubro de 2026

Quarkus 3.40.1, Temurin JDK 25.0.2, Maven 3.9.9 e `maven.compiler.release=25` (bytecode Java 25). Comando executado: `mvn -o test package` com cache Maven local da tarefa. Passaram 10 testes, incluindo 2 HTTP JWT, com ORM offline e sem conexão PostgreSQL. Resultado: 10 testes, 0 falhas, 0 erros, 0 ignorados. O pacote JVM `target/quarkus-app/quarkus-run.jar` foi criado. Nenhum Docker, build nativo ou banco foi iniciado. A integração real com PostgreSQL ainda depende de validação em banco didático isolado.
