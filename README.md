# MCP Banking Demo — Monorepo

A minimal agent-based banking demo using Spring Boot + Spring AI.

## Services

| Service | Port | Description |
|---|---|---|
| `agent-service` | 8080 | LLM agent loop — loads skills from YAML, orchestrates tool calls via Spring AI |
| `retail-bank-server` | 8081 | Banking tool server — REST endpoints with MCP-style UI schemas |

## Features

- **Balance Inquiry**: Natural language → `getBalance` tool → `balance_card` UI schema
- **Fund Transfer**: Natural language → `reviewTransfer` → user confirms → `executeTransfer` → `transfer_success` UI schema

## Architecture

```
Angular (UI renderer)
       ↕
agent-service  (Spring Boot + Spring AI + Claude)
       ↕
retail-bank-server  (Spring Boot — banking tools + UI schemas)
```

## Running

### 1. Start retail-bank-server (no env vars needed)
```bash
cd retail-bank-server
mvn spring-boot:run
```

### 2. Start agent-service
```bash
export ANTHROPIC_API_KEY=your-key-here
cd agent-service
mvn spring-boot:run
```

## API

### Send a message
```
POST http://localhost:8080/api/agent/message
Content-Type: application/json

{ "sessionId": "demo-1", "message": "What is my balance?" }
```

### Confirm a transfer
```
POST http://localhost:8080/api/agent/confirm
Content-Type: application/json

{ "sessionId": "demo-1", "action": "CONFIRM_TRANSFER" }
```

## Response shape

```json
{
  "uiSchema": {
    "type": "balance_card",
    "data": { "account": "Savings", "balance": 250000, "currency": "LKR" },
    "actions": null
  },
  "awaitingConfirmation": false,
  "cancelled": false,
  "error": false,
  "errorMessage": null
}
```

## UI Schema types

| Type | Trigger | Has actions? |
|---|---|---|
| `balance_card` | getBalance tool | No |
| `transfer_review` | reviewTransfer tool | Yes (Confirm / Cancel) |
| `transfer_success` | executeTransfer tool | No |

## Demo payees (mock data)

| Name | Key |
|---|---|
| Amal Perera | "amal" |
| Kumari Silva | "kumari" |
| Nimal Karunaratne | "nimal" |

## Skills

Skills are YAML files under `agent-service/src/main/resources/skills/`. Each file defines intent keywords and system instructions injected into the LLM prompt. Add a new `.yaml` file to extend agent capabilities.
