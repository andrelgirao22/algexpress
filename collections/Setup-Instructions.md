# 🚀 Instruções de Setup - AlgExpress API Collections

## 📋 Arquivos Disponíveis

Na pasta `collections/` você encontrará:

- 📄 **AlgExpress-Postman-Collection.json** - Coleção para Postman
- 📄 **AlgExpress-Insomnia-Collection.json** - Coleção para Insomnia v11.4.0+  
- 📖 **API-Testing-Roadmap.md** - Roteiro completo de testes
- 📖 **Setup-Instructions.md** - Este arquivo

---

## 🔧 Setup para Postman

### **1. Instalar Postman**
- Download: https://www.postman.com/downloads/
- Versão recomendada: Mais recente disponível

### **2. Importar Coleção**

#### **Método 1: Interface Gráfica**
1. Abrir Postman
2. Clicar em **Import** (canto superior esquerdo)
3. Clicar em **Upload Files** 
4. Selecionar `AlgExpress-Postman-Collection.json`
5. Clicar em **Import**

#### **Método 2: Drag & Drop**
1. Abrir Postman
2. Arrastar o arquivo `AlgExpress-Postman-Collection.json` para a interface
3. Confirmar a importação

### **3. Configurar Variáveis de Ambiente**

#### **Opção A: Usar Variáveis Incluídas**
As variáveis já estão pré-configuradas na coleção:
- `base_url`: `http://localhost:8080`
- `customer_id`: `1` 
- `order_id`: `1`
- `pizza_id`: `1`
- Etc.

#### **Opção B: Criar Environment Personalizado**
1. Clicar no ícone ⚙️ (Settings) → **Manage Environments**
2. Clicar em **Add**
3. Nome: `AlgExpress Local`
4. Adicionar variáveis:

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| `base_url` | `http://localhost:8080` | `http://localhost:8080` |
| `customer_id` | `1` | `1` |
| `order_id` | `1` | `1` |
| `pizza_id` | `1` | `1` |
| `address_id` | `1` | `1` |
| `payment_id` | `1` | `1` |
| `delivery_id` | `1` | `1` |

5. **Salvar** o environment
6. **Selecionar** o environment no dropdown superior direito

### **4. Configurar Scripts Automáticos (Opcional)**

Para capturar IDs automaticamente, adicione em **Tests**:

```javascript
// Para endpoints que retornam um objeto com ID
if (pm.response.code === 200 || pm.response.code === 201) {
    var jsonData = pm.response.json();
    if (jsonData.id) {
        // Ajustar a variável conforme o endpoint
        pm.environment.set("customer_id", jsonData.id);
    }
}
```

---

## 🛠️ Setup para Insomnia

### **1. Instalar Insomnia**
- Download: https://insomnia.rest/download
- **Versão necessária**: 11.4.0 ou superior
- Versões anteriores podem ter incompatibilidades

### **2. Verificar Versão**
1. Abrir Insomnia
2. Menu **Insomnia** → **About Insomnia** (macOS) ou **Help** → **About** (Windows/Linux)
3. Confirmar que a versão é 11.4.0+

### **3. Importar Coleção**

#### **Método 1: Interface Gráfica**
1. Abrir Insomnia
2. Clicar no **+** ao lado de workspaces
3. Selecionar **Import**
4. Clicar em **From File**
5. Selecionar `AlgExpress-Insomnia-Collection.json`
6. Clicar em **Import**

#### **Método 2: Drag & Drop**
1. Abrir Insomnia
2. Arrastar o arquivo `AlgExpress-Insomnia-Collection.json` para a interface
3. Confirmar a importação

### **4. Configurar Environment**

O environment já está incluído na coleção importada com as variáveis:

```json
{
  "base_url": "http://localhost:8080",
  "customer_id": "1",
  "order_id": "1", 
  "pizza_id": "1",
  "address_id": "1",
  "payment_id": "1",
  "delivery_id": "1",
  "delivery_person_id": "1"
}
```

#### **Para Editar Variáveis:**
1. Clicar no dropdown do environment (canto superior esquerdo)
2. Selecionar **Manage Environments**
3. Editar o environment **Base Environment**
4. Modificar valores conforme necessário
5. **Save**

### **5. Configurar Template Tags (Opcional)**

Para dados dinâmicos, use template tags:
- **Data/Hora atual**: `{{ timestamp }}`
- **UUID aleatório**: `{{ uuid }}`
- **Variável**: `{{ _.base_url }}`

---

## ⚡ Configuração Rápida (Quick Start)

### **1. Pré-requisitos**
```bash
# Verificar se a aplicação está rodando
curl http://localhost:8080/actuator/health
```

### **2. Importação Expressa**

#### **Postman:**
```bash
# Abrir Postman e importar via drag&drop
# Arquivo: AlgExpress-Postman-Collection.json
```

#### **Insomnia:**
```bash
# Abrir Insomnia e importar via drag&drop  
# Arquivo: AlgExpress-Insomnia-Collection.json
```

### **3. Primeiro Teste**
Executar: `GET /api/v1/clientes` para verificar conectividade

---

## 🔍 Verificação do Setup

### **Checklist Postman:**
- [ ] Coleção **AlgExpress API Collection** aparece na sidebar
- [ ] Folders organizados: Customers, Menu, Orders, Payments, Deliveries  
- [ ] Variáveis configuradas no environment
- [ ] Request de teste retorna resposta válida

### **Checklist Insomnia:**
- [ ] Workspace **AlgExpress API Collection** criado
- [ ] Requests organizados por funcionalidade
- [ ] Environment **Base Environment** ativo
- [ ] Request de teste retorna resposta válida

---

## 🆘 Troubleshooting

### **Problemas Comuns:**

#### **Postman:**

**Erro: "Could not get response"**
- ✅ Verificar se aplicação está rodando em `http://localhost:8080`
- ✅ Verificar variável `base_url` no environment
- ✅ Desabilitar proxy se necessário (Settings → Proxy)

**Variáveis não funcionam**
- ✅ Confirmar environment selecionado no dropdown
- ✅ Verificar sintaxe: `{{variable_name}}`
- ✅ Verificar se variáveis existem no environment

#### **Insomnia:**

**Erro na importação**
- ✅ Verificar versão do Insomnia (deve ser 11.4.0+)
- ✅ Arquivo JSON não deve estar corrompido
- ✅ Tentar importar novamente

**Template tags não funcionam**
- ✅ Verificar sintaxe: `{{ _.variable_name }}`
- ✅ Confirmar environment ativo
- ✅ Verificar se variável existe no environment

#### **Ambas as Ferramentas:**

**Aplicação não responde**
```bash
# Verificar se aplicação está rodando
netstat -an | findstr :8080  # Windows
lsof -i :8080               # macOS/Linux

# Iniciar aplicação se necessário
mvn spring-boot:run
```

**Erros 404/500**
- ✅ Verificar endpoints no código dos controllers
- ✅ Consultar logs da aplicação
- ✅ Verificar se banco de dados está configurado

---

## 🎯 Próximos Passos

1. **✅ Setup concluído** → Prosseguir para `API-Testing-Roadmap.md`
2. **🧪 Executar testes** seguindo a sequência definida no roteiro
3. **📊 Documentar resultados** dos testes executados
4. **🐛 Reportar bugs** encontrados durante os testes

---

## 📞 Suporte

Para dúvidas sobre:
- **Configuração**: Consultar documentação oficial das ferramentas
- **API**: Verificar controllers e documentação Swagger
- **Bugs**: Logs da aplicação e debugging

---

**🎉 Setup concluído! Agora você pode testar a API AlgExpress! 🍕**