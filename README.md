# BeCare API 🌿

**Grupo:** 

| Nome           | RM       |
|----------------|----------|
| Caio Freitas   | RM553190 |
| Caio Hideki    | RM553630 |
| Jorge Booz     | RM552700 |
| Mateus Tibão   | RM553267 |
| Lana Andrade   | RM552596 |

---

## 📖 Descrição do Projeto

O **BeCare** é um módulo de bem-estar integrado ao ecossistema da Care Plus. Beneficiários publicam *microações saudáveis* do dia a dia — foto de hidratação, check-in de caminhada, momento de meditação — classificadas em um dos **7 Pilares de bem-estar**:

| # | Pilar | Descrição |
|---|-------|-----------|
| 1 | Prevenção | Check-ups, vacinas, exames preventivos |
| 2 | Alimentação | Hábitos alimentares equilibrados |
| 3 | Hidratação | Consumo adequado de água |
| 4 | Atividade Física | Exercícios, caminhadas, treinos |
| 5 | Saúde Mental | Mindfulness, meditação, equilíbrio emocional |
| 6 | Engajamento Social | Conexões, atividades em grupo |
| 7 | Sono | Qualidade e quantidade de descanso |

Os posts passam por moderação (IA + humano), e o usuário acumula **insígnias** e participa de **desafios colaborativos**. Ao final de cada semana, um **relatório personalizado** com mensagem motivacional é gerado e pode ser enviado via WhatsApp Business API.

---

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                     Cliente (HTTP)                       │
│         Postman / Insomnia / Swagger UI                  │
└────────────────────────┬────────────────────────────────┘
                         │ REST / JSON
┌────────────────────────▼────────────────────────────────┐
│                  Controller Layer                        │
│  UserController · PostController · PillarController      │
│  BadgeController · ChallengeController · ReportController│
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                   Service Layer                          │
│  UserService · PostService · BadgeService                │
│  ChallengeService · ReportService                        │
└────────────────────────┬────────────────────────────────┘
                         │ JPA / Spring Data
┌────────────────────────▼────────────────────────────────┐
│                 Repository Layer                         │
│  UserRepository · PostRepository · PillarRepository      │
│  BadgeRepository · ChallengeRepository · ReportRepository│
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│              Banco de Dados (H2 / SQL Server)            │
│          Migrações gerenciadas pelo Flyway               │
└─────────────────────────────────────────────────────────┘
```

# Diagramas:

## Diagrama de Arquitetura em camadas
<img width="2033" height="8192" alt="Arquitetura em camadas" src="https://github.com/user-attachments/assets/ae407a3a-04dd-4fb9-8825-dcd7ec9fe7f5" />

## Diagrama ER
<img width="8192" height="3825" alt="Diagrama ER" src="https://github.com/user-attachments/assets/0ae377c3-9830-4780-892b-73b30640fd3d" />

## Diagrama de Fluxo — Ciclo de vida de um Post
<img width="2870" height="8192" alt="Fluxo do post" src="https://github.com/user-attachments/assets/a35e10e2-5d9c-4e15-973f-78c91f99cb1f" />

# Swagger

## Página 1
<img width="1464" height="761" alt="image" src="https://github.com/user-attachments/assets/c6f13375-f47b-4199-a2f8-2f50885b5ff0" />

## Página 2
<img width="1552" height="743" alt="image" src="https://github.com/user-attachments/assets/1c5eb9e4-99b2-41ac-9739-40146af57292" />

## Página 3
<img width="1554" height="747" alt="image" src="https://github.com/user-attachments/assets/967a681d-affc-489b-b5c9-7fbaac301f9d" />

## Página 4
<img width="1564" height="407" alt="image" src="https://github.com/user-attachments/assets/369cb3f0-866b-4f0a-895a-e99c8bf3dfe3" />


### Padrões utilizados
- **SOA** – serviços independentes por domínio (Users, Posts, Badges, Challenges, Reports)
- **REST** – endpoints versionados em `/api/v1/`
- **Camadas** – Controller → Service → Repository (separação clara de responsabilidades)
- **DTO** – objetos de request/response separados das entidades JPA
- **VO** – `PillarStats` (Value Object imutável para estatísticas dos pilares)
- **ResponseEntity** – padronização de status HTTP em todos os endpoints
- **RestControllerAdvice** – tratamento centralizado de exceções

---

## ⚙️ Tecnologias

| Tecnologia | Versão | Finalidade |
|-----------|--------|-----------|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.2.5 | Framework base |
| Spring Data JPA | 3.2.x | Persistência ORM |
| Spring Validation | 3.2.x | Bean Validation (JSR-380) |
| H2 Database | runtime | Banco em memória (dev/teste) |
| Flyway | 9.x | Migrações de banco |
| Lombok | latest | Redução de boilerplate |
| Springdoc OpenAPI | 2.5.0 | Swagger UI |
| JUnit 5 + AssertJ | 5.x | Testes de integração |
| Maven | 3.9+ | Build e dependências |

---

## 🚀 Como executar

### Pré-requisitos
- Java 21+
- Maven 3.9+

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/seu-grupo/becare-api.git
cd becare-api

# 2. Compile e execute
mvn spring-boot:run

# 3. Acesse a documentação interativa
# Swagger UI:  http://localhost:8080/swagger-ui.html
# OpenAPI JSON: http://localhost:8080/api-docs
# H2 Console:  http://localhost:8080/h2-console
#   → JDBC URL: jdbc:h2:mem:becaredb | User: sa | Senha: (vazia)
```

### Executar testes

```bash
mvn test
```

---

## 📡 Endpoints da API

### Usuários `/api/v1/users`

| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/v1/users` | Cadastrar beneficiário |
| `GET` | `/api/v1/users` | Listar todos |
| `GET` | `/api/v1/users/{id}` | Buscar por ID |
| `PUT` | `/api/v1/users/{id}` | Atualizar dados |
| `DELETE` | `/api/v1/users/{id}` | Desativar (soft delete) |

### Posts `/api/v1/posts`

| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/v1/posts` | Publicar ação saudável |
| `GET` | `/api/v1/posts/feed` | Feed público paginado |
| `GET` | `/api/v1/posts/{id}` | Buscar por ID |
| `GET` | `/api/v1/posts/user/{userId}` | Posts de um usuário |
| `PATCH` | `/api/v1/posts/{id}/moderate` | Moderar post |
| `DELETE` | `/api/v1/posts/{id}` | Remover post |

### Pilares `/api/v1/pillars`

| Método | URL | Descrição |
|--------|-----|-----------|
| `GET` | `/api/v1/pillars` | Listar os 7 pilares |
| `GET` | `/api/v1/pillars/{id}` | Buscar pilar por ID |

### Insígnias `/api/v1/badges`

| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/v1/badges/award/{userId}/{type}` | Conceder insígnia |
| `GET` | `/api/v1/badges/user/{userId}` | Insígnias do usuário |

### Desafios `/api/v1/challenges`

| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/v1/challenges` | Criar desafio |
| `GET` | `/api/v1/challenges` | Listar todos |
| `GET` | `/api/v1/challenges/active` | Somente ativos |
| `GET` | `/api/v1/challenges/{id}` | Buscar por ID |
| `POST` | `/api/v1/challenges/{id}/join/{userId}` | Entrar no desafio |
| `DELETE` | `/api/v1/challenges/{id}` | Desativar |

### Relatórios `/api/v1/reports`

| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/api/v1/reports/generate/{userId}` | Gerar relatório semanal |
| `GET` | `/api/v1/reports/user/{userId}` | Histórico de relatórios |
| `GET` | `/api/v1/reports/{id}` | Buscar por ID |

---

## 📋 Exemplos de Requisições e Respostas

### Criar usuário

**Request**
```http
POST /api/v1/users
Content-Type: application/json

{
  "name": "Ana Lima",
  "email": "ana@careplus.com.br",
  "cpf": "123.456.789-00",
  "birthDate": "1995-06-15"
}
```

**Response** `201 Created`
```json
{
  "id": 1,
  "name": "Ana Lima",
  "email": "ana@careplus.com.br",
  "cpf": "123.456.789-00",
  "birthDate": "1995-06-15",
  "active": true,
  "createdAt": "2025-05-04T10:30:00"
}
```

### Publicar ação saudável

**Request**
```http
POST /api/v1/posts
Content-Type: application/json

{
  "userId": 1,
  "pillarId": 3,
  "description": "Tomei 2 litros de água hoje! 💧 #Hidratação",
  "mediaUrl": "https://cdn.careplus.com.br/posts/agua.jpg",
  "visibility": "PUBLIC"
}
```

**Response** `201 Created`
```json
{
  "id": 1,
  "userId": 1,
  "userName": "Ana Lima",
  "pillarId": 3,
  "pillarName": "Hidratação",
  "description": "Tomei 2 litros de água hoje! 💧 #Hidratação",
  "status": "PENDING",
  "visibility": "PUBLIC",
  "createdAt": "2025-05-04T10:35:00"
}
```

### Moderar post

**Request**
```http
PATCH /api/v1/posts/1/moderate
Content-Type: application/json

{
  "status": "APPROVED",
  "note": "Conteúdo positivo e alinhado às diretrizes."
}
```

**Response** `200 OK`
```json
{
  "id": 1,
  "status": "APPROVED",
  "moderationNote": "Conteúdo positivo e alinhado às diretrizes.",
  ...
}
```

### Erro de validação (exemplo)

**Response** `400 Bad Request`
```json
{
  "timestamp": "2025-05-04T10:40:00",
  "status": 400,
  "error": "Erro de validação",
  "fieldErrors": {
    "email": "Formato de e-mail inválido.",
    "cpf": "CPF deve seguir o formato 000.000.000-00."
  }
}
```

---

## 🗄️ Diagrama de Entidades (ER)

```
users ──────────────────────── posts
  │  1                    N       │
  │                               │ N:1
  │                           pillars
  │ 1                             │
  │                               │
badges (N)            weekly_reports (N)
  │                               
  │ N:N (challenge_participants)  
challenges ──────────────────────┘
```

**Tabelas:**
- `users` – beneficiários da Care Plus
- `pillars` – os 7 pilares (seed via Flyway)
- `posts` – ações saudáveis publicadas
- `badges` – insígnias conquistadas
- `challenges` – desafios colaborativos
- `challenge_participants` – N:N users ↔ challenges
- `weekly_reports` – relatórios semanais gerados

---

## 🔍 Observações

- O banco H2 é em memória e reinicia com a aplicação. Para produção, configure PostgreSQL ou SQL Server nas `application.properties`.
- A moderação por IA está representada na arquitetura TOGAF (Sprint 1/2); nesta sprint, a moderação é exposta via endpoint REST (`PATCH /posts/{id}/moderate`).
- O envio de relatório via WhatsApp Business API é um serviço externo; o campo `sentWhatsapp` na entidade `WeeklyReport` registra o status do envio.
- Todos os métodos sensíveis possuem logs em nível `DEBUG`/`INFO` via Slf4j.
