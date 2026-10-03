# Validação do Dia 3

`mvn test package` com Quarkus 3.40.1, JDK 17.0.18 e Maven 3.9.9:
4 testes unitários, 0 falhas, 0 erros. O pacote JVM foi criado. No build,
`DB_URL` apontou para porta fictícia 65535; não houve conexão PostgreSQL.
JPA, transações, relações e rotas HTTP compilaram, mas não foram testados
contra banco. Nenhum container foi iniciado.
