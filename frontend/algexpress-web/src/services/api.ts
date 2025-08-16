// Configuração base do Axios para comunicação com API Spring Boot
// Centraliza interceptors, headers e configurações de requisição
import axios, { InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para requisições (adicionar auth token automaticamente)
api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  // Adicionar token de autenticação se existir
  const token = localStorage.getItem('token');
  if (token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor para respostas (tratamento global de erros)
api.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    // Tratamento global de erros
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default api;
