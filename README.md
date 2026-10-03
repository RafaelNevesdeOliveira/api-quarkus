# Agência Bancária em Quarkus — Dia 3, JPA e contas

Cópia incremental dos dias 1 e 2. Preserva o esquema PostgreSQL original em
`sql/01_criar_tabelas.sql`. JDBC manual saiu da implementação; as entidades
Jakarta Persistence e repositories Panache assumem a persistência. A entidade
`ContaBancaria` e seus DTOs foram trazidos do domínio bancário Spring.

## Comparação com o projeto Spring

`JpaRepository<Pessoa, Long>` do Spring oferecia `save` e consultas derivadas.
Aqui, `PessoaRepositoryJpa` usa `PanacheRepository<Pessoa>`, `persistAndFlush`
e `count("cpf", cpf)`. O service conserva uma fronteira `PessoaRepository`.
`@Transactional` abre a unidade de trabalho; a entidade muda o saldo e o
Hibernate grava a alteração no commit. `@ManyToOne` associa conta a Pessoa e
TipoConta. O JSON de `ContaBancariaResponse` é plano para evitar expor proxies
ou ciclos.

## Rotas desta etapa

- `POST /api/pessoas` cadastra titular.
- `POST /api/contas` abre conta com titular e tipo existentes.
- `GET /api/contas/{id}` e `GET /api/contas/pessoa/{pessoaId}` consultam.
- `PATCH /api/contas/{id}/depositos` e `/saques` usam regras da entidade.

O service coordena, mas `ContaBancaria` recusa movimentação não positiva, conta
inativa e saque acima do saldo. O esquema SQL impõe saldo não negativo e
unicidade de agência+número.

## Banco didático isolado

Execute o esquema somente numa instância PostgreSQL de aula isolada. As
variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD` são obrigatórias. Não foram
incluídas senhas reais. Dev Services está desabilitado e
`quarkus.hibernate-orm.schema-management.strategy=none` impede que a aplicação
crie ou altere tabelas. A carga sintética é opcional; ela cria uma Pessoa, um
TipoConta e uma conta para exercícios. Não aponte para banco compartilhado.

## Verificação realizada

Quarkus 3.40.1, JDK 17 e Maven 3.9.9: `mvn test package` passou com **4 testes
unitários** de `ContaBancaria`. O build JVM foi gerado. Sem PostgreSQL nesta
tarefa, ainda não foram executados testes HTTP ou de integração JPA, nem
verificado o mapeamento contra uma instância real.
