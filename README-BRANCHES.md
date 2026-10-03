# Um projeto Quarkus, cinco aulas cumulativas

Este repositório didático é independente do Spring original. Os commits formam
uma linha cumulativa: `aluno-inicio` → `dia-01` → `dia-02` →
`dia-03` → `dia-04` → `dia-05`. Os nomes são exatos. As branches
`dia-01` a `dia-05` são marcos com **respostas do professor**. A branch
`main` contém as cinco aulas em sequência. O repositório GitHub é público:
quem o abrir poderá ver também os gabaritos das aulas seguintes.

| Aula | Base do desafio sem resposta do próprio dia | Gabarito ao final |
|---|---|---|
| Dia 1 | `aluno-inicio` | `dia-01` |
| Dia 2 | `dia-01` | `dia-02` |
| Dia 3 | `dia-02` | `dia-03` |
| Dia 4 | `dia-03` | `dia-04` |
| Dia 5 | `dia-04` | `dia-05` |

Para prática independente, distribua somente o ZIP do estado inicial daquele
dia, exportado da branch da coluna do meio. O ZIP não contém o histórico nem
as respostas posteriores. No Dia 1, `aluno-inicio` é o projeto vazio gerado pelo
plugin Maven Quarkus 3.40.1, com REST Jackson e Hibernate Validator.

## Clonar e navegar

```sh
git clone https://github.com/RafaelNevesdeOliveira/api-quarkus.git
cd api-quarkus
git branch -a
git switch dia-01
git log --oneline --all --graph --decorate
```

Para preparar o desafio seguinte, parta da branch do dia anterior, por
exemplo `git switch dia-02` antes da aula 3. Para trabalhar sem alterar o
gabarito, crie sua própria branch com `git switch -c meu-trabalho-dia-03`.
Antes de trocar de branch, confira `git status --short` e registre seu
trabalho em um commit local. Se houver mudanças não registradas, conclua
o commit ou use `git stash push -u` e depois `git stash pop` na branch
correta. Uma troca de branch bloqueada pelo Git indica arquivos que seriam
sobrescritos; não force a troca. Os comentários no código explicam as
anotações Quarkus/Jakarta junto das equivalentes Spring.

Os PowerPoints, documentos Word e ZIPs de desafio ficam nas pastas locais
`AULA_01_...` a `AULA_05_...` em `outputs/aulas-quarkus-agencia-bancaria-2026`;
o GitHub contém o código-fonte.
