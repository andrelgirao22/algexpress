React está rodando em: http://localhost:3000

  🔍 O que você verá no navegador:

  - Título "AlgExpress - Sistema de Pizzaria"
  - Contador interativo que aumenta quando você clica
  - Lista dos próximos passos

  🛠️ Comandos úteis:

  cd frontend/algexpress-web

  npm start          # Inicia desenvolvimento (já rodando)
  npm run build      # Cria versão para produção
  npm test           # Executa testes

  📝 Próximos Passos:

  1. Experimentar: Clique no botão e veja o contador aumentar
  2. Editar: Mude algo no App.tsx e veja a atualização automática
  3. Criar telas: Usar os protótipos HTML como base para componentes React

  O React está funcionando perfeitamente! Qualquer alteração que você fizer no código será automaticamente refletida no navegador (hot reload).


no arquivo package.json

  📜 SCRIPTS DISPONÍVEIS

  "scripts": {
    "start": "react-scripts start",    // ← Servidor de desenvolvimento
    "build": "react-scripts build",    // ← Build para produção
    "test": "react-scripts test",      // ← Executar testes
    "eject": "react-scripts eject"     // ← Ejetar configurações (irreversível)
  }

  🔍 O que cada comando faz:

  - npm start → react-scripts start
    - Inicia servidor desenvolvimento (porta 3000)
    - Hot reload automático
    - Detecta erros em tempo real
  - npm run build → react-scripts build
    - Gera versão otimizada para produção
    - Minifica código, otimiza imagens
    - Cria pasta build/ com arquivos estáticos
  - npm test → react-scripts test
    - Executa testes unitários
    - Modo watch (re-executa ao salvar)
  - npm run eject → react-scripts eject
    - ⚠️ CUIDADO: Irreversível!
    - Expõe configurações do Webpack/Babel
    - Só use se precisar de customizações avançadas
