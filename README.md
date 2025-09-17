# ConnectHub Backend

O **ConnectHub Backend** é o núcleo servidor da plataforma ConnectHub, um ambiente colaborativo para integração de projetos acadêmicos, gamificação, comunicação e aprendizado. Construído com **Quarkus**, o framework Supersônico e Subatômico para Java, o sistema adota práticas modernas de arquitetura, incluindo DDD (Domain-Driven Design), e oferece uma API REST robusta e extensível.

---

## Índice

- [Visão Geral do Projeto](#visão-geral-do-projeto)
- [Arquitetura e Padrões](#arquitetura-e-padrões)
  - [Domain-Driven Design (DDD)](#domain-driven-design-ddd)
  - [Organização dos Pacotes](#organização-dos-pacotes)
- [Principais Funcionalidades](#principais-funcionalidades)
  - [Gestão de Projetos](#gestão-de-projetos)
  - [Módulo Acadêmico](#módulo-acadêmico)
  - [Gamificação](#gamificação)
  - [Comunicação e Notificações](#comunicação-e-notificações)
- [Tecnologias e Dependências](#tecnologias-e-dependências)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Segurança e Autenticação](#segurança-e-autenticação)
- [Referências e Guias](#referências-e-guias)

---

## Visão Geral do Projeto

Este backend serve como base para recursos como publicação e comentários de projetos, fóruns acadêmicos (tópicos e respostas), gamificação (XP, níveis, conquistas), notificações e gerenciamento de usuários. É altamente modularizado, facilitando a manutenção e a evolução.

---

## Arquitetura e Padrões

### Domain-Driven Design (DDD)

O projeto segue o conceito de DDD para isolar responsabilidades e promover clareza de domínio. Os principais pacotes são:

- `domain`: Entidades de negócio, enums e lógica de domínio.
- `application`: Serviços de aplicação e lógica de orquestração.
- `infrastructure`: Exceções, configurações e integrações externas.
- `interfaces.rest`: Exposição de endpoints RESTful.

### Organização dos Pacotes

- `domain.project`: Modelos de Projeto e Comentários.
- `domain.academic`: Entidades de Tópico e Resposta para fóruns.
- `domain.gamification`: Tipos e lógica de XP (Experiência).
- `application.user`: Serviços para manipulação de usuários.
- `application.communication`: Serviços de notificação.
- `infrastructure.exception`: Manipulação de erros.
- `interfaces.rest`: Controladores REST para API pública.

---

## Principais Funcionalidades

### Gestão de Projetos

- Entidades como `Project` e `ProjectComment` permitem cadastro, visualização e comentários em projetos.
- Enum `ProjectTechnologies` padroniza tecnologias relacionadas a projetos, incluindo Java, Python, Docker, frameworks JS etc.

### Módulo Acadêmico

- Entidades `Topic` e `Answer` suportam fóruns com tópicos, respostas, likes, dislikes e marcação de solução.
- Sistema de XP para participação (criar tópicos, responder, melhor resposta).

### Gamificação

- Sistema de XP (`XpType`) e níveis.
- Eventos planejados para evolução de nível do usuário.

### Comunicação e Notificações

- Serviço de notificações para eventos do sistema (criação, deleção, contagem).

---

## Tecnologias e Dependências

Utiliza extensivamente as features do Quarkus, incluindo:

- **Quarkus REST** ([guia](https://quarkus.io/guides/rest)): Endpoints reativos.
- **Hibernate ORM com Panache** ([guia](https://quarkus.io/guides/hibernate-orm-panache)): ORM simplificado.
- **Hibernate Validator** ([guia](https://quarkus.io/guides/validation)): Validação de dados.
- **SmallRye OpenAPI** ([guia](https://quarkus.io/guides/openapi-swaggerui)): Documentação automática com Swagger UI.
- **Jackson**: Serialização JSON.
- **JDBC Driver - PostgreSQL** ([guia](https://quarkus.io/guides/datasource)): Driver JDBC para PostgreSQL.

Outras dependências comuns do ecossistema Quarkus podem ser incluídas conforme necessidade.

---

## Como Executar

### Ambiente de Desenvolvimento

```shell
./mvnw quarkus:dev
```
Acesse a Dev UI em [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/).

### Build e Execução

**Empacotamento padrão:**
```shell
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

**Über-jar:**
```shell
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

**Build Nativo:**
```shell
./mvnw package -Dnative
# Ou com container:
./mvnw package -Dnative -Dquarkus.native.container-build=true
./target/connect-hub-1.0.0-SNAPSHOT-runner
```

---

## Testes

Inclui testes automatizados de endpoints REST com Quarkus Test e RestAssured.

---

## Segurança e Autenticação

O código sugere a intenção de uso de hash de senhas (ex: BCrypt), mas há trechos comentados. Recomenda-se revisar e garantir a implementação de hashing seguro das senhas e autenticação via JWT ou OAuth2, conforme padrões Quarkus.

---

## Referências e Guias

- [Quarkus](https://quarkus.io/)
- [Hibernate ORM Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [OpenAPI/Swagger](https://quarkus.io/guides/openapi-swaggerui)
- [Quarkus REST](https://quarkus.io/guides/rest)

---

## Contato

Para dúvidas, sugestões ou contribuir, abra uma issue ou pull request neste repositório.

---
