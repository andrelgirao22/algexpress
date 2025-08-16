// Serviço para operações relacionadas a pedidos
// Integração com endpoints /api/v1/pedidos da API Spring Boot
import api from './api';

export const orderService = {
  // Buscar todos os pedidos (resumido)
  getAll: () => api.get('/pedidos'),
  
  // Buscar pedido por ID (detalhado)
  getById: (id: number) => api.get(`/pedidos/${id}`),
  
  // Buscar pedidos por status
  getByStatus: (status: string) => api.get(`/pedidos/status/${status}`),
  
  // Pedidos de hoje
  getToday: () => api.get('/pedidos/today'),
  
  // Criar novo pedido
  create: (orderData: any) => api.post('/pedidos', orderData),
  
  // Atualizar status do pedido
  updateStatus: (id: number, status: string) => 
    api.patch(`/pedidos/${id}/status?status=${status}`),
  
  // Cancelar pedido
  cancel: (id: number) => api.patch(`/pedidos/${id}/cancel`),
};
