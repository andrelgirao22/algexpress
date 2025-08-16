// Tipos relacionados à autenticação
// Baseados nos tipos do backend Spring Boot

export enum UserRole {
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER', 
  EMPLOYEE = 'EMPLOYEE',
  DELIVERY_PERSON = 'DELIVERY_PERSON'
}

export interface User {
  id: number;
  name: string;
  email: string;
  role: UserRole;
  active: boolean;
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: User;
  expiresIn: number;
}

export interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  loading: boolean;
}