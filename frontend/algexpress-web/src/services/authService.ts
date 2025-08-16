// Serviço de autenticação
// Integração com endpoints de auth da API Spring Boot
import api from './api';
import { LoginRequest, LoginResponse, User, UserRole } from '../features/auth/types/auth';

// Mock temporário para desenvolvimento (remover quando API estiver pronta)
const mockUsers = [
  {
    id: 1,
    name: 'Admin AlgExpress',
    email: 'admin@algexpress.com',
    role: UserRole.ADMIN,
    active: true,
    createdAt: new Date().toISOString()
  },
  {
    id: 2,
    name: 'Manager João',
    email: 'manager@algexpress.com',
    role: UserRole.MANAGER,
    active: true,
    createdAt: new Date().toISOString()
  }
];

export const authService = {
  // Login do usuário (mock para desenvolvimento)
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    // Simular delay da API
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // Verificar credenciais mock
    const user = mockUsers.find(u => u.email === credentials.email);
    if (!user || credentials.password !== '123456') {
      throw new Error('Credenciais inválidas');
    }

    return {
      token: 'mock-jwt-token-' + Date.now(),
      user,
      expiresIn: 3600
    };

    // Versão real (descomentar quando API estiver pronta)
    // const response = await api.post<LoginResponse>('/auth/login', credentials);
    // return response.data;
  },

  // Logout (invalidar token no servidor)
  logout: async (): Promise<void> => {
    // Mock - apenas simular delay
    await new Promise(resolve => setTimeout(resolve, 500));
    
    // Versão real (descomentar quando API estiver pronta)
    // await api.post('/auth/logout');
  },

  // Validar token atual
  validateToken: async (): Promise<User> => {
    // Mock - validar token fake
    const token = localStorage.getItem('token');
    if (!token || !token.startsWith('mock-jwt-token')) {
      throw new Error('Token inválido');
    }

    const userStr = localStorage.getItem('user');
    if (!userStr) {
      throw new Error('Usuário não encontrado');
    }

    return JSON.parse(userStr);

    // Versão real (descomentar quando API estiver pronta)
    // const response = await api.get<User>('/auth/me');
    // return response.data;
  },

  // Refresh token
  refreshToken: async (): Promise<LoginResponse> => {
    const response = await api.post<LoginResponse>('/auth/refresh');
    return response.data;
  },

  // Alterar senha
  changePassword: async (currentPassword: string, newPassword: string): Promise<void> => {
    await api.post('/auth/change-password', {
      currentPassword,
      newPassword
    });
  }
};