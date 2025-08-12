# 🍕 AlgExpress API - Roteiro de Testes

## 📋 Pré-requisitos

### 1. **Ambiente de Desenvolvimento**
- ✅ Java 21 instalado
- ✅ Maven configurado
- ✅ PostgreSQL rodando (ou H2 para testes)
- ✅ Aplicação Spring Boot executando em `http://localhost:8080`

### 2. **Ferramentas de Teste**
- **Postman**: Versão mais recente
- **Insomnia**: Versão 11.4.0 ou superior

### 3. **Importar Coleções**
- **Postman**: Importar `AlgExpress-Postman-Collection.json`
- **Insomnia**: Importar `AlgExpress-Insomnia-Collection.json`

---

## 🚀 Sequência de Testes (Ordem Obrigatória)

### **FASE 1: Configuração Inicial**

#### 1.1. Verificar Aplicação
```http
GET http://localhost:8080/actuator/health
```
**Resultado esperado**: Status 200 - `{"status":"UP"}`

---

### **FASE 2: Gerenciamento de Clientes** 👥

#### 2.1. **Criar Cliente** (Obrigatório primeiro)
```http
POST /api/v1/clientes
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao.silva@email.com", 
  "phone": "11999999999",
  "cpf": "12345678901"
}
```
**📝 Anotar**: `customer_id` retornado na resposta

#### 2.2. **Buscar Cliente Criado**
```http
GET /api/v1/clientes/{customer_id}
```

#### 2.3. **Adicionar Endereço ao Cliente** (Obrigatório para delivery)
```http
POST /api/v1/enderecos/{customer_id}
Content-Type: application/json

{
  "street": "Rua das Flores",
  "number": "123", 
  "complement": "Apto 45",
  "neighborhood": "Centro",
  "city": "São Paulo",
  "state": "SP",
  "zipCode": "01234567",
  "type": "RESIDENTIAL",
  "referencePoints": "Próximo ao mercado"
}
```
**📝 Anotar**: `address_id` retornado na resposta

#### 2.4. **Listar Todos os Clientes**
```http
GET /api/v1/clientes
```

---

### **FASE 3: Gerenciamento do Menu** 🍕

#### 3.1. **Listar Pizzas Disponíveis**
```http
GET /api/v1/cardapio/pizzas
```
**📝 Anotar**: `pizza_id` de uma pizza existente

#### 3.2. **Buscar Pizza Específica**
```http
GET /api/v1/cardapio/pizzas/{pizza_id}
```

#### 3.3. **Listar Ingredientes**
```http
GET /api/v1/cardapio/ingredientes
```
**📝 Anotar**: IDs de ingredientes para customização

#### 3.4. **Criar Nova Pizza** (Opcional)
```http
POST /api/v1/cardapio/pizzas
Content-Type: application/json

{
  "name": "Pizza Margherita",
  "description": "Pizza clássica com molho de tomate, mussarela e manjericão",
  "category": "TRADITIONAL",
  "priceSmall": 25.90,
  "priceMedium": 35.90,
  "priceLarge": 45.90,
  "priceExtraLarge": 55.90,
  "preparationTimeMinutes": 15,
  "available": true
}
```

---

### **FASE 4: Gestão de Pedidos** 📋

#### 4.1. **Criar Pedido** (Usar dados das fases anteriores)
```http
POST /api/v1/pedidos
Content-Type: application/json

{
  "customerId": {customer_id},
  "type": "DELIVERY",
  "addressId": {address_id},
  "observations": "Sem cebola na pizza",
  "items": [
    {
      "pizzaId": {pizza_id},
      "size": "MEDIUM",
      "quantity": 1,
      "additionalIngredientIds": [],
      "removedIngredientIds": [],
      "observations": "Bem assada"
    }
  ]
}
```
**📝 Anotar**: `order_id` retornado na resposta

#### 4.2. **Buscar Pedido Criado**
```http
GET /api/v1/pedidos/{order_id}
```

#### 4.3. **Confirmar Pedido**
```http
PATCH /api/v1/pedidos/{order_id}/confirm
```

#### 4.4. **Atualizar Status do Pedido**
```http
PATCH /api/v1/pedidos/{order_id}/status?status=PREPARING
```

#### 4.5. **Marcar como Pronto**
```http
PATCH /api/v1/pedidos/{order_id}/ready
```

#### 4.6. **Listar Pedidos do Cliente**
```http
GET /api/v1/pedidos/customer/{customer_id}
```

#### 4.7. **Listar Pedidos de Hoje**
```http
GET /api/v1/pedidos/today
```

---

### **FASE 5: Processamento de Pagamentos** 💳

#### 5.1. **Processar Pagamento**
```http
POST /api/v1/pagamentos
Content-Type: application/json

{
  "orderId": {order_id},
  "paymentMethodType": "CREDIT_CARD",
  "amount": 45.90,
  "amountPaid": 45.90
}
```
**📝 Anotar**: `payment_id` retornado na resposta

#### 5.2. **Aprovar Pagamento**
```http
POST /api/v1/pagamentos/{payment_id}/approve
```

#### 5.3. **Buscar Pagamentos do Pedido**
```http
GET /api/v1/pagamentos/order/{order_id}
```

#### 5.4. **Listar Pagamentos Pendentes**
```http
GET /api/v1/pagamentos/pending
```

---

### **FASE 6: Gestão de Entregas** 🚚

#### 6.1. **Criar Entrega**
```http
POST /api/v1/entregas
Content-Type: application/json

{
  "orderId": {order_id},
  "deliveryPersonId": 1
}
```
**📝 Anotar**: `delivery_id` retornado na resposta

#### 6.2. **Buscar Entrega**
```http
GET /api/v1/entregas/{delivery_id}
```

#### 6.3. **Listar Todas as Entregas**
```http
GET /api/v1/entregas
```

#### 6.4. **Finalizar Pedido (Marcar como Entregue)**
```http
PATCH /api/v1/pedidos/{order_id}/delivered
```

---

## 🧪 Cenários de Teste Adicionais

### **Cenário 1: Pedido para Retirada**
```http
POST /api/v1/pedidos
Content-Type: application/json

{
  "customerId": {customer_id},
  "type": "PICKUP",
  "observations": "Para retirar às 19h",
  "items": [...]
}
```

### **Cenário 2: Pedido com Customizações**
```http
POST /api/v1/pedidos
Content-Type: application/json

{
  "customerId": {customer_id},
  "type": "DELIVERY",
  "addressId": {address_id},
  "items": [
    {
      "pizzaId": {pizza_id},
      "size": "LARGE",
      "quantity": 1,
      "additionalIngredientIds": [2, 5],
      "removedIngredientIds": [3],
      "observations": "Extra queijo, sem cebola"
    }
  ]
}
```

### **Cenário 3: Cancelamento de Pedido**
```http
PATCH /api/v1/pedidos/{order_id}/cancel
```

---

## ✅ Checklist de Validações

### **Para Cada Endpoint:**
- [ ] **Status Code**: Verificar códigos de resposta corretos
- [ ] **Response Body**: Validar estrutura JSON retornada
- [ ] **Headers**: Verificar Content-Type e outros headers
- [ ] **Timing**: Avaliar tempo de resposta (< 2s para operações simples)

### **Validações Específicas:**

#### **Clientes:**
- [ ] Email único não permite duplicatas
- [ ] CPF único não permite duplicatas
- [ ] Campos obrigatórios são validados

#### **Pedidos:**
- [ ] Não permite pedido sem items
- [ ] Valida se pizza existe
- [ ] Calcula total corretamente
- [ ] Endereço obrigatório para delivery

#### **Pagamentos:**
- [ ] Não permite valor negativo
- [ ] Valor não pode ser maior que o total do pedido
- [ ] Status do pagamento atualiza corretamente

---

## 🚨 Casos de Erro para Testar

### **Códigos de Erro Esperados:**

#### **400 - Bad Request**
```http
POST /api/v1/clientes
Content-Type: application/json

{
  "name": "",
  "email": "email-inválido"
}
```

#### **404 - Not Found**
```http
GET /api/v1/clientes/99999
```

#### **409 - Conflict**
```http
POST /api/v1/clientes
Content-Type: application/json

{
  "name": "Outro Cliente",
  "email": "joao.silva@email.com",  // Email já existe
  "phone": "11888888888",
  "cpf": "12345678901"  // CPF já existe
}
```

---

## 📊 Variáveis de Ambiente

### **Configurar no Postman/Insomnia:**

| Variável | Valor Inicial | Descrição |
|----------|---------------|-----------|
| `base_url` | `http://localhost:8080` | URL base da API |
| `customer_id` | `1` | ID do cliente (atualizar após criar) |
| `order_id` | `1` | ID do pedido (atualizar após criar) |
| `pizza_id` | `1` | ID da pizza (verificar no menu) |
| `address_id` | `1` | ID do endereço (atualizar após criar) |
| `payment_id` | `1` | ID do pagamento (atualizar após criar) |
| `delivery_id` | `1` | ID da entrega (atualizar após criar) |

---

## 🎯 Dicas de Uso

### **Postman:**
1. Use **Variables** para armazenar IDs retornados
2. Configure **Tests** para extrair IDs automaticamente:
   ```javascript
   var jsonData = pm.response.json();
   pm.environment.set("customer_id", jsonData.id);
   ```
3. Use **Pre-request Scripts** para dados dinâmicos

### **Insomnia:**
1. Use **Environment Variables** para IDs
2. Configure **Response** → **Extract** para capturar valores
3. Use **Template Tags** para dados dinâmicos

---

## ⚠️ Notas Importantes

1. **Ordem de Execução**: Siga rigorosamente a sequência para evitar dependências não resolvidas
2. **IDs Dinâmicos**: Sempre atualize as variáveis com IDs reais retornados pela API
3. **Dados de Teste**: Use dados realistas mas únicos para evitar conflitos
4. **Cleanup**: Após testes, considere limpar dados criados no banco
5. **Monitoramento**: Observe logs da aplicação para debugar problemas

---

## 🆘 Troubleshooting

### **Problemas Comuns:**

- **Connection Refused**: Verificar se aplicação está rodando
- **404 Not Found**: Verificar se endpoints estão corretos  
- **500 Internal Server Error**: Verificar logs da aplicação
- **Validation Errors**: Verificar formato dos dados enviados
- **Foreign Key Constraints**: Verificar se IDs referenciados existem

---

**✨ Boa sorte com os testes! 🚀**