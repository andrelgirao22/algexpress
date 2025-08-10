# AlgExpress 🍕

Sistema completo de gerenciamento de pedidos e entregas para pizzarias, desenvolvido com Spring Boot e Java 21.

## 📋 Sobre o Projeto

AlgExpress é uma aplicação web robusta projetada para otimizar o processo de gestão de uma pizzaria, desde o cadastro de produtos até a entrega final ao cliente. O sistema oferece controle completo sobre pedidos, clientes, cardápio e logística de entregas.

## ⚡ Principais Funcionalidades

### 👥 Gestão de Clientes
- Cadastro completo de clientes
- Histórico de pedidos por cliente
- Sistema de fidelização

### 📍 Gerenciamento de Endereços
- Cadastro e validação de endereços de entrega
- Múltiplos endereços por cliente
- Cálculo automático de distância e taxa de entrega

### 🍕 Cardápio e Produtos
- CRUD completo de pizzas
- Gestão de ingredientes e suas combinações
- Controle de preços e promoções
- Categorização de produtos

### 📦 Sistema de Pedidos
- Interface intuitiva para criação de pedidos
- Acompanhamento de status em tempo real
- Cálculo automático de valores e taxas
- Histórico completo de transações

### 🚚 Controle de Entregas
- Gestão de entregadores
- Otimização de rotas
- Cálculo dinâmico de taxa de entrega baseado na distância
- Rastreamento de entregas

### 💰 Sistema Financeiro
- Cálculo automático de taxas de entrega
- Relatórios de vendas
- Controle de pagamentos

## 🛠 Tecnologias Utilizadas

- **Java 21** - Linguagem principal
- **Spring Boot 3.x** - Framework web
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **PostgreSQL** - Banco de dados
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização

## 🏗 Arquitetura

### Domain-Driven Design (DDD) - Abordagem Pragmática

O projeto utiliza os conceitos de DDD de forma pragmática, focando na clareza do domínio de negócio sem over-engineering:

#### Bounded Contexts
- **Pedidos** - Gestão de pedidos e itens
- **Clientes** - Cadastro e relacionamento com clientes
- **Cardápio** - Produtos, pizzas e ingredientes
- **Entregas** - Logística e cálculo de taxas
- **Financeiro** - Pagamentos e taxas

#### Camadas da Aplicação
```
┌─ Presentation Layer (Controllers/API)
├─ Application Layer (Services/Use Cases)
├─ Domain Layer (Entities/Value Objects)
└─ Infrastructure Layer (Repositories/External Services)
```

### Entidades Principais (Domain)
- **Cliente** - Aggregate root para dados do cliente
- **Endereço** - Value object para localização
- **Pizza** - Entity do domínio cardápio
- **Ingrediente** - Value object para composição
- **Pedido** - Aggregate root para transações
- **Entrega** - Entity do domínio logística

### API RESTful
A aplicação expõe endpoints REST organizados por bounded context:

```
/api/v1/clientes     - Gestão de clientes
/api/v1/enderecos    - Endereços de entrega
/api/v1/cardapio     - Pizzas e ingredientes
/api/v1/pedidos      - Criação e acompanhamento
/api/v1/entregas     - Controle logístico
/api/v1/relatorios   - Dados analíticos
```

### Padrões Implementados
- **Domain-Driven Design** (pragmático)
- **Repository Pattern**
- **Application Services**
- **Domain Services**
- **Value Objects**
- **Aggregate Pattern**
- **DTO (Data Transfer Object)**
- **Builder Pattern**
- **RESTful API**

## 📊 Funcionalidades Detalhadas

### Dashboard Administrativo
- Visão geral de pedidos do dia
- Métricas de performance
- Status de entregas em andamento

### API RESTful
- Endpoints documentados com OpenAPI/Swagger
- Versionamento de API
- Tratamento de exceções padronizado

### Sistema de Relatórios
- Vendas por período
- Performance de entregadores
- Análise de produtos mais vendidos
- Relatório de taxas de entrega

## 🚀 Como Executar

### Pré-requisitos
- Java 21 instalado
- Maven 3.8+
- Banco de dados (MySQL/PostgreSQL)
- Docker (opcional)

### Configuração
1. Clone o repositório
2. Configure as variáveis de ambiente no `application.properties`
3. Execute as migrations do banco de dados
4. Inicie a aplicação

### Variáveis de Ambiente
```properties
# Database
DB_URL=jdbc:mysql://localhost:3306/algexpress
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha

# API Configuration
API_VERSION=v1
SERVER_PORT=8080

# Delivery Settings
DEFAULT_DELIVERY_RATE=5.00
MAX_DELIVERY_DISTANCE=20
```

## 📈 Roadmap

### Versão 1.0 (MVP)
- [x] CRUD de entidades básicas
- [x] Sistema de pedidos
- [x] Cálculo de taxas de entrega
- [x] API RESTful

### Versão 2.0
- [ ] Interface web responsiva
- [ ] Sistema de notificações
- [ ] Integração com sistemas de pagamento
- [ ] App mobile para entregadores

### Versão 3.0
- [ ] Inteligência artificial para otimização de rotas
- [ ] Sistema de avaliações
- [ ] Programa de fidelidade avançado
- [ ] Analytics e Business Intelligence

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Desenvolvedor

Desenvolvido com ❤️ por [Seu Nome]

---

**AlgExpress** - Transformando a gestão de pizzarias com tecnologia e eficiência! 🚀