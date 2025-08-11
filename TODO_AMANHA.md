# 📋 TODO LIST - PRÓXIMA FASE DE DESENVOLVIMENTO

## 🎯 Objetivo: Implementar Camada de Serviços e API REST

### 📦 1. SERVICES LAYER
Criar services para todas as entidades com regras de negócio:

#### 1.1 Customer Services
- [ ] `CustomerService` - CRUD, busca por telefone/email, sistema de pontos fidelidade
- [ ] `AddressService` - Gerenciamento de endereços, cálculo de taxas de entrega

#### 1.2 Menu Services  
- [ ] `PizzaService` - CRUD de pizzas, controle de disponibilidade, preços por tamanho
- [ ] `IngredientService` - Gerenciamento de ingredientes, preços adicionais

#### 1.3 Order Services
- [ ] `OrderService` - Criação de pedidos, cálculo de totais, mudança de status
- [ ] `OrderItemService` - Gestão de itens, ingredientes adicionais/removidos
- [ ] `PaymentService` - Processamento de pagamentos múltiplos, cálculo de troco

#### 1.4 Delivery Services
- [ ] `DeliveryService` - Controle de entregas, tempos, tentativas
- [ ] `DeliveryPersonService` - Gestão de entregadores, disponibilidade, turnos

#### 1.5 User Services
- [ ] `UserService` - Autenticação, autorização, controle de tentativas de login

### 🌐 2. REST CONTROLLERS
Criar controllers RESTful seguindo padrões REST:

#### 2.1 Customer Controllers
- [ ] `CustomerController` - `/api/v1/customers`
- [ ] `AddressController` - `/api/v1/addresses`

#### 2.2 Menu Controllers
- [ ] `PizzaController` - `/api/v1/pizzas` 
- [ ] `IngredientController` - `/api/v1/ingredients`

#### 2.3 Order Controllers
- [ ] `OrderController` - `/api/v1/orders`
- [ ] `PaymentController` - `/api/v1/payments`

#### 2.4 Delivery Controllers  
- [ ] `DeliveryController` - `/api/v1/deliveries`
- [ ] `DeliveryPersonController` - `/api/v1/delivery-persons`

#### 2.5 User Controllers
- [ ] `UserController` - `/api/v1/users`
- [ ] `AuthController` - `/api/v1/auth` (login, logout)

### 📚 3. API DOCUMENTATION
Documentação completa da API:

#### 3.1 OpenAPI/Swagger Setup
- [ ] Adicionar dependência `springdoc-openapi-starter-webmvc-ui`
- [ ] Configurar Swagger UI em `/swagger-ui.html`
- [ ] Configurar OpenAPI JSON em `/v3/api-docs`

#### 3.2 Annotations Documentation
- [ ] `@Operation` para cada endpoint
- [ ] `@ApiResponse` para códigos de resposta
- [ ] `@Schema` para DTOs
- [ ] `@Parameter` para parâmetros de entrada

#### 3.3 DTOs Creation
- [ ] Request DTOs para cada operação (Create, Update)
- [ ] Response DTOs para retorno de dados
- [ ] Error DTOs para tratamento de exceções

### 🔧 4. API CLIENT COLLECTIONS

#### 4.1 Postman Collection
- [ ] Criar workspace "AlgExpress API"
- [ ] Organizar por pastas (Customers, Menu, Orders, Delivery, Auth)
- [ ] Configurar variáveis de ambiente (base_url, tokens)
- [ ] Exemplos de requests para todos os endpoints
- [ ] Testes automatizados para responses

#### 4.2 Insomnia Collection
- [ ] Criar workspace "AlgExpress"  
- [ ] Estrutura similar ao Postman
- [ ] Environment para dev/prod
- [ ] Requests com exemplos de payload

### 🛡️5. EXCEPTION HANDLING
Sistema robusto de tratamento de erros:

#### 5.1 Global Exception Handler
- [ ] `@ControllerAdvice` para tratamento global
- [ ] Custom exceptions para regras de negócio
- [ ] Padronização de response de erro
- [ ] Logging de exceções

#### 5.2 Custom Exceptions
- [ ] `CustomerNotFoundException`
- [ ] `PizzaUnavailableException`  
- [ ] `InsufficientPaymentException`
- [ ] `DeliveryPersonNotAvailableException`

### 🧪 6. VALIDATION & TESTING
Validações e testes unitários:

#### 6.1 Bean Validation
- [ ] `@Valid` nos controllers
- [ ] `@NotNull`, `@NotBlank`, `@Email` etc. nos DTOs
- [ ] Custom validators para regras específicas

#### 6.2 Unit Tests
- [ ] Testes para services com Mockito
- [ ] Testes para controllers com MockMvc
- [ ] Testes para repositories com @DataJpaTest

### 📊 7. BUSINESS FEATURES
Funcionalidades de negócio específicas:

#### 7.1 Order Management
- [ ] Calcular automaticamente taxas de entrega por distância
- [ ] Validar disponibilidade de ingredientes
- [ ] Sistema de desconto e promoções
- [ ] Tempo estimado de entrega

#### 7.2 Delivery Optimization
- [ ] Algoritmo de atribuição automática de entregadores
- [ ] Cálculo de rotas otimizadas
- [ ] Notificações de status de entrega

#### 7.3 Reports & Analytics
- [ ] Relatório de vendas por período
- [ ] Performance de entregadores
- [ ] Ingredientes mais populares
- [ ] Análise de pagamentos

---

## 🚀 ORDEM DE EXECUÇÃO RECOMENDADA

### Dia 1: Foundation
1. Exception Handling
2. DTOs Creation
3. Customer & Address Services + Controllers

### Dia 2: Core Business
1. Menu Services + Controllers (Pizza/Ingredient)
2. Order Services + Controllers
3. Payment Services + Controllers

### Dia 3: Delivery & Users
1. Delivery Services + Controllers
2. User Services + Controllers 
3. Auth Controller

### Dia 4: Documentation & Testing
1. OpenAPI/Swagger Setup
2. Postman Collection
3. Insomnia Collection
4. Unit Tests

### Dia 5: Advanced Features
1. Business Logic Implementation
2. Validation Enhancement
3. Performance Optimization
4. Final Testing

---

## ✅ CRITÉRIOS DE ACEITAÇÃO

- [ ] API totalmente funcional com todos os CRUDs
- [ ] Documentação Swagger completa e navegável
- [ ] Collections Postman/Insomnia funcionando
- [ ] Tratamento de erros padronizado
- [ ] Testes unitários com cobertura > 80%
- [ ] Validações de entrada implementadas
- [ ] Regras de negócio da pizzaria funcionando

**Meta: API REST completa e documentada para gerenciamento de pizzaria!** 🍕