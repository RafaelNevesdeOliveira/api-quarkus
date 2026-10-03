# Agência Bancária em Quarkus — início do aluno

## Ambiente JDK 25.0.2 e JVM

Use Temurin JDK 25.0.2 para compilar, testar e executar este projeto. O `pom.xml` define `maven.compiler.release=25`: o bytecode gerado exige Java 25. No macOS, selecione o JDK antes do Maven:

```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 25.0.2)
export PATH="$JAVA_HOME/bin:$PATH"
java -version
javac -version
mvn -version
```

Para desenvolver, use `mvn quarkus:dev`. Para criar e executar o pacote JVM, use `mvn test package` e `java -jar target/quarkus-app/quarkus-run.jar` com a pasta `target/quarkus-app` inteira. O curso não usa Docker nem compilação nativa.

Projeto Java 25 gerado do zero pelo plugin Maven Quarkus 3.40.1, com
`rest-jackson` e `hibernate-validator`. Ainda não há entidade, service,
repository ou endpoint bancário: este é o ponto de partida do desafio do
Dia 1. Leia `README-BRANCHES.md` para a progressão das aulas.

Comando validado para gerar este scaffold:

```sh
mvn io.quarkus.platform:quarkus-maven-plugin:3.40.1:create -DprojectGroupId=br.edu.fiap -DprojectArtifactId=agencia-bancaria-quarkus -DprojectVersion=1.0.0-SNAPSHOT -Dextensions=rest-jackson,hibernate-validator -DjavaVersion=25 -DnoCode
```

O gerador cria exemplos em `src/main/docker` e `.dockerignore`. Eles foram
removidos deste scaffold porque as aulas executam somente na JVM.
