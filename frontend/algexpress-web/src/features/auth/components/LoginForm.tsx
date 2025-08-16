import React, { useState } from 'react';
import '../../../styles/LoginForm.css';

interface LoginFormProps {
  onLogin: (email: string, password: string) => Promise<void>;
  loading?: boolean;
}

export const LoginForm = ({ onLogin, loading = false }: LoginFormProps) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    
    if (!email || !password) {
      setError('Por favor, preencha todos os campos');
      return;
    }

    try {
      await onLogin(email, password);
    } catch (err) {
      setError('Email ou senha inválidos');
    }
  };

  return (
    <div className="login-container">
      {/* Fundo com padrão de pizza */}
      <div className="login-background">
        <div className="pizza-pattern"></div>
      </div>
      
      {/* Card de login */}
      <div className="login-card animate-fade-in">
        {/* Logo e header */}
        <div className="login-header">
          <div className="logo-container">
            <div className="pizza-icon">🍕</div>
            <h1 className="brand-name">AlgExpress</h1>
          </div>
          <p className="brand-subtitle">
            Sistema de Gestão da Pizzaria
          </p>
        </div>
        
        {/* Formulário */}
        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email" className="form-label">
              📧 Email
            </label>
            <input
              id="email"
              name="email"
              type="email"
              autoComplete="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="form-input"
              placeholder="seu@email.com"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="password" className="form-label">
              🔒 Senha
            </label>
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="current-password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="form-input"
              placeholder="••••••••"
            />
          </div>

          {error && (
            <div className="error-message animate-slide-in">
              ⚠️ {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="login-button"
          >
            {loading ? (
              <>
                <span className="loading-spinner animate-spin">⏳</span>
                Entrando...
              </>
            ) : (
              <>
                🚀 Entrar
              </>
            )}
          </button>

          {/* Informações de usuários padrão */}
          <div className="login-info">
            <div className="info-card">
              <h4>👨‍💼 Usuários de Teste</h4>
              <div className="user-examples">
                <div className="user-item">
                  <span className="user-role">Admin:</span>
                  <span className="user-email">admin@algexpress.com</span>
                </div>
                <div className="user-item">
                  <span className="user-role">Gerente:</span>
                  <span className="user-email">manager@algexpress.com</span>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
      
      {/* Features highlights */}
      <div className="features-preview">
        <div className="feature-item">
          <div className="feature-icon">📱</div>
          <span>Gestão de Pedidos</span>
        </div>
        <div className="feature-item">
          <div className="feature-icon">🚚</div>
          <span>Controle de Entregas</span>
        </div>
        <div className="feature-item">
          <div className="feature-icon">📊</div>
          <span>Relatórios Completos</span>
        </div>
      </div>
    </div>
  );
};