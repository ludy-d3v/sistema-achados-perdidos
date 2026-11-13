# Sistema de Achados e Perdidos

![Java](https://img.shields.io/badge/Java_21-ED8B00?logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?logo=mysql&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?logo=apachenetbeanside&logoColor=white)

Trabalho prático de **Programação Orientada a Objetos (POO)** e **Laboratório de Banco de Dados** — Engenharia de Software, Universidade Católica de Brasília.

**Orientadores:** João Evangelista (POO) e Jefferson (Laboratório de Banco de Dados).

**Autores:** Ludmilla Oliveira, Miguel Dourado, Lucas Naunn, Miguel Guimarães, Rick Guedes, Thiago Benvenuto.

Sistema desktop em Java para registrar objetos perdidos e encontrados no campus, do cadastro do objeto até a devolução ao dono.

**Tecnologias:** Java 21, Swing (JFrame), padrão MVC, JDBC e MySQL 8.

## Funcionalidades

| Perfil | O que faz |
|---|---|
| **Aluno** | Procura objetos por nome, categoria e data, reivindica um objeto (recebe protocolo `REIV-AAAA-NNNNNN`) e acompanha suas reivindicações. |
| **Funcionário** | Cadastra, edita e exclui objetos, aprova ou recusa reivindicações, registra devoluções (protocolo `DEV-AAAA-NNNNNN`) e gera relatórios por período. |
| **Administrador** | Gerencia funcionários: promove usuários, altera cargo e setor e remove a função de funcionário. |

## Banco de dados

O banco `achados_perdidos` tem 12 tabelas e usa índices, views, triggers, procedures, functions e quatro usuários com privilégios diferentes. A aplicação nunca acessa o banco como `root`: o usuário do MySQL é escolhido conforme o perfil de quem fez login.

| Recurso | Itens |
|---|---|
| Índices | `idx_objeto_categoria_data`, `idx_objeto_data_achado` |
| Views | `reivindicacoes_por_retirante`, `consultar_reivindicacoes` |
| Triggers | `trg_reivindicacao_pendente`, `trg_objeto_devolvido` |
| Procedures | `sp_relatorio_objetos`, `sp_relatorio_reivindicacoes` |
| Functions | `fn_gerar_protocolo_reivindicacao`, `fn_gerar_protocolo_devolucao` |
| Usuários | `app_achados`, `achados_admin`, `achados_funcionario`, `achados_aluno` |

## Como rodar

1. Rode os scripts da pasta [`database`](database) **uma única vez, na ordem dos números**, com um usuário administrador do MySQL:

   | Ordem | Script | Conteúdo |
   |---|---|---|
   | 1 | `01_banco.sql` | Criação do banco e das tabelas |
   | 2 | `02_inserts.sql` | Dados de exemplo |
   | 3 | `03_grupos_usuarios.sql` | Tabelas `grupos_usuarios` e `rel_usuario_grupo` |
   | 4 | `04_indices.sql` | Índices de busca |
   | 5 | `05_views.sql` | Views |
   | 6 | `06_procedures.sql` | Procedures de relatório |
   | 7 | `07_triggers.sql` | Triggers |
   | 8 | `08_seguranca_e_identificadores.sql` | Usuários do banco, privilégios e functions |

2. Abra o projeto no **Apache NetBeans**, adicione o **MySQL Connector/J 8** às bibliotecas e execute a partir de `LoginView`.

3. Entre com um dos usuários de exemplo (senha `123` para todos):

   | Perfil | E-mail |
   |---|---|
   | Aluno | `mariana.alves@a.ucb.br` |
   | Funcionário | `ana.souza@p.ucb.br` |
   | Administrador | `admin.sistema@p.ucb.br` |
   | Administrador e funcionário | `admin.func@p.ucb.br` |

## Estrutura

```
database/   scripts SQL, numerados na ordem de execução
docs/       documentação do projeto
src/        código-fonte (visao, controller, entidade)
```

## Documentação

- [`docs/Documento Técnico - Achados e Perdidos.pdf`](docs/Documento%20T%C3%A9cnico%20-%20Achados%20e%20Perdidos.pdf) (objetivos, metodologia, tecnologias, DER e justificativa de cada recurso do banco).
