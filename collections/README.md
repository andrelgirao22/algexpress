# 🍕 AlgExpress API Collections

Esta pasta contém as coleções de API e documentação para testes do sistema AlgExpress.

## 📁 Arquivos Incluídos

| Arquivo | Descrição | Ferramenta |
|---------|-----------|------------|
| `AlgExpress-Postman-Collection.json` | Coleção completa da API | Postman |
| `AlgExpress-Insomnia-Collection.json` | Coleção completa da API | Insomnia v11.4.0+ |
| `API-Testing-Roadmap.md` | **📋 Roteiro de testes detalhado** | Documentação |
| `Setup-Instructions.md` | **🚀 Instruções de configuração** | Documentação |
| `README.md` | Este arquivo | Documentação |

## 🎯 Como Usar

### **1. Primeiro Acesso**
➡️ **Leia**: `Setup-Instructions.md` para configurar suas ferramentas

### **2. Executar Testes**  
➡️ **Siga**: `API-Testing-Roadmap.md` para testar a API na ordem correta

### **3. Importar Coleções**
- **Postman**: Use `AlgExpress-Postman-Collection.json`
- **Insomnia**: Use `AlgExpress-Insomnia-Collection.json`

## ✨ Recursos Incluídos

### **🔧 Funcionalidades Testáveis**
- ✅ **Gestão de Clientes** - Registro e endereços
- ✅ **Menu de Pizzas** - Pizzas e ingredientes  
- ✅ **Pedidos** - Criação, acompanhamento e status
- ✅ **Pagamentos** - Processamento e aprovação
- ✅ **Entregas** - Tracking e finalização

### **📊 Variáveis Pré-configuradas**
- `base_url` → `http://localhost:8080`
- `customer_id`, `order_id`, `pizza_id`, etc.
- **Todas atualizáveis** conforme seus testes

### **🧪 Cenários de Teste**
- **Fluxo completo** de pedido (cliente → pedido → pagamento → entrega)
- **Casos de erro** (validações, not found, etc.)
- **Customizações** de pizza
- **Diferentes tipos** de pedido (delivery, pickup)

## 🚀 Quick Start

```bash
# 1. Iniciar aplicação
mvn spring-boot:run

# 2. Verificar se está rodando
curl http://localhost:8080/actuator/health

# 3. Importar coleção na sua ferramenta preferida
# 4. Seguir o roteiro de testes
```

## 📋 Endpoints Principais

| Grupo | Endpoints | Exemplo |
|-------|-----------|---------|
| **Clientes** | `/api/v1/clientes` | `GET /api/v1/clientes` |
| **Endereços** | `/api/v1/enderecos` | `POST /api/v1/enderecos/{id}` |
| **Menu** | `/api/v1/cardapio` | `GET /api/v1/cardapio/pizzas` |
| **Pedidos** | `/api/v1/pedidos` | `POST /api/v1/pedidos` |
| **Pagamentos** | `/api/v1/pagamentos` | `POST /api/v1/pagamentos` |
| **Entregas** | `/api/v1/entregas` | `GET /api/v1/entregas` |

## ⚡ Ferramentas Suportadas

### **Postman** 📮
- ✅ Versão mais recente
- ✅ Variáveis automáticas
- ✅ Scripts de teste
- ✅ Environments

### **Insomnia** 🛌  
- ✅ Versão 11.4.0+
- ✅ Template tags
- ✅ Environment variables
- ✅ Response extraction

## 🎨 Organização das Coleções

```
AlgExpress API Collection/
├── 1. Customers Management/
│   ├── Get All Customers
│   ├── Get Customer by ID
│   ├── Create Customer
│   ├── Update Customer
│   └── Add Address to Customer
├── 2. Menu Management/
│   ├── Get All Pizzas
│   ├── Get Pizza by ID
│   ├── Get All Ingredients
│   └── Create Pizza
├── 3. Order Management/
│   ├── Get All Orders
│   ├── Create Order
│   ├── Update Order Status
│   └── [mais endpoints...]
├── 4. Payment Management/
│   └── [endpoints de pagamento...]
└── 5. Delivery Management/
    └── [endpoints de entrega...]
```

## 🆘 Precisa de Ajuda?

1. **Setup**: Consulte `Setup-Instructions.md`
2. **Testes**: Siga `API-Testing-Roadmap.md`  
3. **API Issues**: Verifique logs da aplicação
4. **Bugs**: Documente e reporte

---

**🎉 Pronto para testar! Boa sorte! 🚀**