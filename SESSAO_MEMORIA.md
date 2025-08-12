# Memória da Sessão - AlgExpress

## Instruções de Contexto Atual

### Status do Projeto
- **Projeto**: AlgExpress - Sistema de gestão de pedidos e entregas para pizzarias
- **Tecnologias**: Spring Boot 3.5.4, Java 21, PostgreSQL, Lombok, Maven
- **Arquitetura**: Domain-Driven Design (DDD) com bounded contexts

### Implementações Completas
- ✅ Entidades de Domínio (em inglês)
- ✅ Schema do Banco PostgreSQL
- ✅ Camada de Repositórios completa
- ✅ Configuração do ambiente (.env, .gitignore)
- ✅ Migrações Flyway

### Estrutura de Domínios
1. **Orders** (Pedidos) - Gestão de pedidos e itens
2. **Customers** (Clientes) - Cadastro e relacionamentos
3. **Menu** (Cardápio) - Produtos, pizzas e ingredientes
4. **Deliveries** (Entregas) - Logística e cálculo de taxas
5. **Financial** (Financeiro) - Pagamentos e taxas

### Arquitetura em Camadas
```
┌─ Presentation Layer (Controllers/API)
├─ Application Layer (Services/Use Cases)
├─ Domain Layer (Entities/Value Objects)
└─ Infrastructure Layer (Repositories/External Services)
```

### APIs RESTful Planejadas
- `/api/v1/clientes` - Gestão de clientes
- `/api/v1/enderecos` - Endereços de entrega
- `/api/v1/cardapio` - Pizzas e ingredientes
- `/api/v1/pedidos` - Criação e rastreamento de pedidos
- `/api/v1/entregas` - Controle de entregas
- `/api/v1/relatorios` - Dados analíticos

### Comandos de Desenvolvimento
```bash
# Build e execução
mvn clean compile
mvn test
mvn spring-boot:run
mvn clean package

# Desenvolvimento com auto-reload
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Diretrizes Importantes
- Usar nomes em inglês para entidades
- Lombok para reduzir boilerplate
- Flyway para migrações
- Testes seguindo estrutura do pacote principal
- Seguir princípios DDD pragmáticos
- NUNCA criar arquivos desnecessários
- SEMPRE preferir editar arquivo existente
- NUNCA criar documentação proativamente

### Banco de Dados
- **Produção**: PostgreSQL
- **9 Tabelas Principais**: customers, addresses, pizzas, ingredients, orders, order_items, payments, deliveries, delivery_persons, users
- **Tabelas de Junção**: pizza_ingredients, order_item_additional_ingredients, order_item_removed_ingredients
- **Índices Otimizados** para performance

### Próximos Passos Possíveis
- Implementação da camada de serviços
- Controllers REST
- Autenticação Spring Security
- Testes de integração
- Configurações de ambiente

---
**Data**: 2025-08-11
**Branch**: main (limpa)
**Último Commit**: ed0f78a feat: created services