# AlgExpress API Documentation

Este documento descreve como acessar e usar a documentação da API do AlgExpress.

## Swagger/OpenAPI Documentation

A documentação da API é gerada automaticamente usando Swagger/OpenAPI 3 e está disponível através dos seguintes endpoints:

### Acesso ao Swagger UI

Após iniciar a aplicação, você pode acessar a documentação interativa da API em:

```
http://localhost:8080/swagger-ui.html
```

### JSON da Especificação OpenAPI

Para obter a especificação da API em formato JSON:

```
http://localhost:8080/api-docs
```

## Endpoints Principais

### 🏪 Clientes (`/api/v1/clientes`)
- Gestão completa de clientes
- Gerenciamento de endereços
- Sistema de pontos de fidelidade

### 🍕 Cardápio (`/api/v1/cardapio`)
- Gestão de pizzas e ingredientes
- Configuração de preços por tamanho
- Controle de disponibilidade

### 📦 Pedidos (`/api/v1/pedidos`)
- Criação e gestão de pedidos
- Adição de itens personalizados
- Controle de status

### 💳 Pagamentos (`/api/v1/pagamentos`)
- Processamento de pagamentos
- Múltiplos métodos (Dinheiro, Cartão, PIX)
- Gestão de status e aprovações

### 🚚 Entregas (`/api/v1/entregas`)
- Gestão de entregas
- Controle de entregadores
- Rastreamento de status

### 📊 Relatórios (`/api/v1/relatorios`)
- Relatórios de vendas
- Estatísticas de clientes
- Analytics do negócio

## Como usar

1. **Iniciar a aplicação**:
   ```bash
   mvn spring-boot:run
   ```

2. **Acessar Swagger UI**:
   Abra o navegador e acesse: `http://localhost:8080/swagger-ui.html`

3. **Testar endpoints**:
   - Clique em qualquer endpoint
   - Clique em "Try it out"
   - Preencha os parâmetros necessários
   - Clique em "Execute"

## Autenticação

Atualmente, a API não possui autenticação habilitada. Todos os endpoints são publicamente acessíveis em desenvolvimento.

## Exemplos de Uso

### Criar um Cliente
```bash
curl -X POST "http://localhost:8080/api/v1/clientes" \
     -H "Content-Type: application/json" \
     -d '{
       "name": "João Silva",
       "email": "joao@email.com",
       "phone": "(11) 99999-9999",
       "cpf": "123.456.789-00"
     }'
```

### Buscar Pizzas Disponíveis
```bash
curl -X GET "http://localhost:8080/api/v1/cardapio/pizzas"
```

### Processar um Pagamento
```bash
curl -X POST "http://localhost:8080/api/v1/pagamentos" \
     -H "Content-Type: application/json" \
     -d '{
       "orderId": 1,
       "paymentMethodType": "CASH",
       "amount": 35.90,
       "amountPaid": 40.00
     }'
```

## Configurações Swagger

As configurações do Swagger estão no arquivo `application.properties`:

```properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.tryItOutEnabled=true
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.filter=true
```

## Suporte

Para suporte ou dúvidas sobre a API, entre em contato com a equipe de desenvolvimento.