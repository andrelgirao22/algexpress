// Hook global de autenticação
// Gerencia estado de login, logout e validação de token
import { useState, useEffect, createContext, useContext } from 'react';
import { authService } from '../services/authService';
import { AuthState } from '../features/auth/types/auth';

const AuthContext = createContext<{
  authState: AuthState;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  validateToken: () => Promise<boolean>;
} | null>(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de AuthProvider');
  }
  return context;
};

export const useAuthProvider = () => {
  const [authState, setAuthState] = useState<AuthState>({
    user: null,
    token: null,
    isAuthenticated: false,
    loading: true
  });

  // Login
  const login = async (email: string, password: string): Promise<void> => {
    setAuthState(prev => ({ ...prev, loading: true }));
    
    try {
      const response = await authService.login({ email, password });
      
      // Salvar token no localStorage
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response.user));
      
      setAuthState({
        user: response.user,
        token: response.token,
        isAuthenticated: true,
        loading: false
      });
    } catch (error) {
      setAuthState(prev => ({ ...prev, loading: false }));
      throw error;
    }
  };

  // Logout
  const logout = async () => {
    try {
      await authService.logout();
    } catch (error) {
      console.error('Erro no logout:', error);
    } finally {
      // Limpar dados locais
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      
      setAuthState({
        user: null,
        token: null,
        isAuthenticated: false,
        loading: false
      });
    }
  };

  // Validar token existente
  const validateToken = async (): Promise<boolean> => {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    
    if (!token || !userStr) {
      setAuthState(prev => ({ ...prev, loading: false }));
      return false;
    }

    try {
      // Validar token com servidor
      const user = await authService.validateToken();
      
      setAuthState({
        user,
        token,
        isAuthenticated: true,
        loading: false
      });
      
      return true;
    } catch (error) {
      // Token inválido, limpar dados
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      
      setAuthState({
        user: null,
        token: null,
        isAuthenticated: false,
        loading: false
      });
      
      return false;
    }
  };

  // Verificar autenticação ao carregar
  useEffect(() => {
    validateToken();
  }, []);

  return {
    authState,
    login,
    logout,
    validateToken
  };
};

export { AuthContext };