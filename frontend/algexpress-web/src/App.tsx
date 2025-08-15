import React, { useState } from 'react';
import './App.css';

function App() {
  // Estado para contar cliques
  const [count, setCount] = useState(0);
  
  // Função para incrementar contador
  const handleClick = () => {
    setCount(count + 1);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>🍕 AlgExpress - Sistema de Pizzaria</h1>
        <p>Hello World com React + TypeScript!</p>
        
        <div style={{ margin: '20px 0' }}>
          <p>Você clicou <strong>{count}</strong> vezes</p>
          <button onClick={handleClick}>
            Clique aqui!
          </button>
        </div>
        
        <div style={{ marginTop: '30px' }}>
          <h3>Próximos passos:</h3>
          <ul style={{ textAlign: 'left', maxWidth: '400px' }}>
            <li>✅ React configurado</li>
            <li>🔄 Estado (useState) funcionando</li>
            <li>📝 Próximo: Criar telas do sistema</li>
          </ul>
        </div>
      </header>
    </div>
  );
}

export default App;
