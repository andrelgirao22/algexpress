# 📁 Estrutura do Frontend AlgExpress

## 🎯 Visão Geral

Esta documentação descreve a estrutura de pastas e arquivos do frontend React + TypeScript do sistema AlgExpress. A organização segue as melhores práticas para projetos escaláveis e maintíveis.

## 🏗️ Arquitetura Escolhida

**Padrão Híbrido**: Combina organização por tipo de arquivo (components, hooks) com organização por domínio (features).

### Vantagens:
- ✅ **Escalabilidade**: Fácil adicionar novas features
- ✅ **Reutilização**: Componentes UI compartilhados
- ✅ **Manutenibilidade**: Código organizado por responsabilidade
- ✅ **Testabilidade**: Cada parte pode ser testada isoladamente
- ✅ **TypeScript**: Tipagem forte em toda aplicação

## 📂 Estrutura Detalhada

```
frontend/algexpress-web/src/
├── 📁 components/              # Componentes reutilizáveis
│   ├── 📁 ui/                 # Componentes base (design system)
│   │   ├── button.tsx         # Botão padronizado
│   │   ├── input.tsx          # Input padronizado
│   │   ├── card.tsx           # Card container
│   │   └── table.tsx          # Tabela base
│   ├── 📁 charts/             # Componentes de gráficos
│   │   ├── OrdersChart.tsx    # Gráfico de pedidos
│   │   └── MetricsCards.tsx   # Cards de métricas
│   ├── 📁 forms/              # Formulários reutilizáveis
│   ├── 📁 tables/             # Tabelas específicas
│   └── 📁 layout/             # Componentes de layout
├── 📁 features/               # Funcionalidades por domínio
│   ├── 📁 dashboard/          # Dashboard administrativo
│   │   ├── 📁 components/     # Componentes específicos do dashboard
│   │   ├── 📁 hooks/          # Hooks específicos do dashboard
│   │   └── 📁 types/          # Tipos específicos do dashboard
│   ├── 📁 pedidos/            # Gestão de pedidos
│   │   ├── 📁 components/     # Componentes de pedidos
│   │   │   └── OrdersTable.tsx
│   │   ├── 📁 hooks/          # Hooks de pedidos
│   │   │   └── useOrdersFeature.ts
│   │   ├── 📁 services/       # Serviços específicos de pedidos
│   │   └── 📁 types/          # Tipos específicos de pedidos
│   ├── 📁 cardapio/           # Gestão do menu
│   ├── 📁 clientes/           # Gestão de clientes
│   ├── 📁 entregadores/       # Gestão de entregadores
│   └── 📁 relatorios/         # Relatórios e analytics
├── 📁 services/               # Integração com API
│   ├── api.ts                 # Configuração Axios
│   ├── orderService.ts        # Serviços de pedidos
│   ├── customerService.ts     # Serviços de clientes
│   ├── menuService.ts         # Serviços do cardápio
│   └── deliveryService.ts     # Serviços de entrega
├── 📁 hooks/                  # Custom hooks compartilhados
│   ├── useOrders.ts           # Hook global de pedidos
│   ├── useCustomers.ts        # Hook global de clientes
│   └── useAuth.ts             # Hook de autenticação
├── 📁 types/                  # Tipos TypeScript
│   ├── api.ts                 # Tipos da API (DTOs, Enums)
│   ├── order.ts               # Tipos de pedidos
│   ├── customer.ts            # Tipos de clientes
│   └── pizza.ts               # Tipos de pizzas
├── 📁 utils/                  # Funções auxiliares
│   ├── formatters.ts          # Formatação (moeda, data, telefone)
│   ├── validators.ts          # Validações
│   └── constants.ts           # Constantes da aplicação
├── 📁 lib/                    # Configurações e bibliotecas
│   ├── queryClient.ts         # React Query setup
│   └── utils.ts               # Utilitários (shadcn/ui)
└── 📁 styles/                 # Estilos globais
    ├── globals.css            # CSS global
    └── components.css         # Estilos de componentes
```

## 🔍 Descrição dos Diretórios

### 📁 `/components`
**Componentes reutilizáveis em toda aplicação**

- **`/ui`**: Componentes base do design system (Button, Input, Card, Table)
- **`/charts`**: Componentes de gráficos para dashboards
- **`/forms`**: Formulários genéricos reutilizáveis
- **`/tables`**: Tabelas específicas mas reutilizáveis
- **`/layout`**: Header, Footer, Sidebar, etc.

### 📁 `/features`
**Organização por domínio de negócio**

Cada feature contém sua própria estrutura:
- **`/components`**: Componentes específicos da feature
- **`/hooks`**: Hooks específicos da feature
- **`/services`**: Serviços específicos (se necessário)
- **`/types`**: Tipos específicos da feature

**Features principais:**
- **`dashboard`**: Painel administrativo com métricas
- **`pedidos`**: Gestão completa de pedidos
- **`cardapio`**: Gestão de pizzas e ingredientes
- **`clientes`**: Gestão de clientes e endereços
- **`entregadores`**: Gestão de entregadores e rotas
- **`relatorios`**: Relatórios e analytics

### 📁 `/services`
**Integração com API Spring Boot**

- **`api.ts`**: Configuração base do Axios com interceptors
- **`orderService.ts`**: Operações de pedidos (/api/v1/pedidos)
- **`customerService.ts`**: Operações de clientes (/api/v1/clientes)
- **`menuService.ts`**: Operações do cardápio (/api/v1/cardapio)
- **`deliveryService.ts`**: Operações de entrega (/api/v1/entregas)

### 📁 `/hooks`
**Custom hooks compartilhados**

Hooks que podem ser utilizados por múltiplas features:
- **`useOrders.ts`**: Gerenciamento global de pedidos
- **`useCustomers.ts`**: Gerenciamento global de clientes
- **`useAuth.ts`**: Autenticação e autorização

### 📁 `/types`
**Tipagem TypeScript**

- **`api.ts`**: DTOs e Enums baseados na API Spring Boot
- **`order.ts`**: Tipos específicos de pedidos
- **`customer.ts`**: Tipos específicos de clientes
- **`pizza.ts`**: Tipos específicos de pizzas

### 📁 `/utils`
**Funções utilitárias**

- **`formatters.ts`**: Formatação de moeda, data, telefone, CPF
- **`validators.ts`**: Validações de formulários
- **`constants.ts`**: Constantes da aplicação

### 📁 `/lib`
**Configurações de bibliotecas**

- **`queryClient.ts`**: Configuração do React Query
- **`utils.ts`**: Utilitários do shadcn/ui

## 🔄 Fluxo de Dados

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Components    │    │     Hooks       │    │    Services     │
│   (UI Layer)    │◄──►│  (State Mgmt)   │◄──►│  (API Layer)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         ▲                       ▲                       ▲
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     Types       │    │     Utils       │    │  API Spring     │
│   (TypeScript)  │    │   (Helpers)     │    │     Boot        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📝 Convenções de Nomenclatura

### Arquivos e Pastas:
- **Componentes**: `PascalCase.tsx` (ex: `OrdersTable.tsx`)
- **Hooks**: `camelCase.ts` com prefixo `use` (ex: `useOrders.ts`)
- **Services**: `camelCase.ts` com sufixo `Service` (ex: `orderService.ts`)
- **Types**: `camelCase.ts` (ex: `api.ts`, `order.ts`)
- **Utils**: `camelCase.ts` (ex: `formatters.ts`)
- **Pastas**: `lowercase` ou `kebab-case`

### Exports:
- **Componentes**: Named exports `export const ComponentName`
- **Hooks**: Named exports `export const useHookName`
- **Services**: Object exports `export const serviceName = {}`
- **Types**: Named exports `export interface TypeName`

## 🚀 Próximos Passos

### Implementação Recomendada:

1. **Fase 1 - Base**:
   - Implementar componentes UI básicos
   - Configurar services e API integration
   - Criar tipos TypeScript fundamentais

2. **Fase 2 - Features Core**:
   - Dashboard com métricas
   - Gestão de pedidos (CRUD completo)
   - Listagem de clientes

3. **Fase 3 - Features Avançadas**:
   - Gestão de cardápio
   - Sistema de entregadores
   - Relatórios e analytics

4. **Fase 4 - Otimizações**:
   - Autenticação e autorização
   - Otimizações de performance
   - Testes unitários e integração

## 🔧 Comandos Úteis

```bash
# Desenvolvimento
npm run dev

# Build
npm run build

# Testes
npm run test

# Lint
npm run lint

# Type check
npm run type-check
```

## 📚 Referências

- [React Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Next.js Documentation](https://nextjs.org/docs)
- [Tailwind CSS](https://tailwindcss.com/docs)
- [React Query](https://tanstack.com/query/latest)

---

Esta estrutura garante um projeto escalável, maintível e seguindo as melhores práticas da comunidade React + TypeScript.