# Agência Bancária em Quarkus — Dia 2, JDBC manual

Cópia incremental do Dia 1. O contrato `POST /api/pessoas` e os DTOs permanecem;
`PessoaRepositoryEmMemoria` foi substituído por `PessoaRepositoryJdbc`.

## O que mudou

`PessoaRepositoryJdbc` recebe um `DataSource`, abre `Connection`, prepara SQL com
`?`, vincula parâmetros, lê o `ResultSet` e fecha recursos com try-with-resources.
Usa `public.pessoas` do **mesmo** `sql/01_criar_tabelas.sql` da referência
Spring. `INSERT ... RETURNING id` é sintaxe PostgreSQL. O SQLSTATE `23505`
traduz a restrição UNIQUE de CPF em conflito 409, inclusive se duas chamadas
passarem ao mesmo tempo pela checagem prévia do service.

No Spring original, `PessoaRepository extends JpaRepository` oferece `save`
e `existsByCpf`; essa interface esconde SQL, conexão e mapeamento. O Dia 3
mostrará essa abstração novamente com JPA no Quarkus. Quarkus não exige os
pacotes `controller/service/repository`; mantemos a estrutura por clareza.

## Banco didático isolado

Crie um banco PostgreSQL **novo e isolado** para a aula. Não execute os scripts
em banco real ou compartilhado. A ordem é `sql/01_criar_tabelas.sql` e, se
quiser, `sql/02_carga_dados_sintetica.sql`. O esquema original contém também
`tipos_conta`, `usuarios` e `contas_bancarias`; hoje a API acessa somente
`pessoas`.

Antes de iniciar a API, defina `DB_URL`, `DB_USER` e `DB_PASSWORD` para essa
instância. O projeto não fornece credenciais nem inicia Dev Services ou Docker.

```sh
export DB_URL='jdbc:postgresql://HOST_DIDATICO:5432/agencia_bancaria_didatica'
export DB_USER='USUARIO_DIDATICO'
export DB_PASSWORD='SENHA_DIDATICA'
mvn quarkus:dev
```

A porta HTTP é 8081. `requests.http` contém exemplos de cadastro e erros.

## Verificação realizada

`mvn test package` passou com **2 testes unitários** de `PessoaService`, usando
um repository falso. O código JDBC e o empacotamento JVM compilaram, mas a
conexão, os comandos SQL e os endpoints HTTP **não foram executados contra
PostgreSQL** nesta tarefa. Nenhum banco ou container foi iniciado.
