# Frontend React - Guia de Desenvolvimento

## 📋 Visão Geral

Este documento fornece diretrizes completas para desenvolver o frontend React que consumirá a API Spring Boot do AlgExpress. O sistema é um dashboard administrativo para pizzaria com foco em gestão de pedidos, entregadores e métricas operacionais.

## 🎯 Análise dos Protótipos

### Características do Design Atual:
- **Layout administrativo profissional** usando Tailwind CSS
- **Dashboard com métricas** (pedidos totais, em andamento, tempo médio)
- **Gráficos e visualizações** (pedidos por dia, tendências de tempo)
- **Tabelas de dados** complexas (pedidos, entregadores)
- **Sistema de mapas** para rastreamento de entregas
- **Design responsivo** com container queries

### Funcionalidades Identificadas:
1. **Dashboard Principal** - Métricas e gráficos em tempo real
2. **Gestão de Pedidos** - Lista, detalhes e atualizações de status
3. **Gestão de Entregadores** - Controle de rotas e disponibilidade
4. **Mapa de Entregas** - Visualização e otimização de rotas
5. **Relatórios** - Análises de performance e histórico

## 🚀 Stack Tecnológica Recomendada

### Framework e Core:
```bash
# Criação do projeto
npx create-next-app@latest algexpress-admin --typescript --tailwind --eslint

# Dependências essenciais
npm install @tanstack/react-query axios
npm install @hookform/resolvers react-hook-form yup
npm install lucide-react date-fns
```

### UI e Componentes:
```bash
# Shadcn/ui (componentes base)
npx shadcn-ui@latest init
npx shadcn-ui@latest add button input card table badge
npx shadcn-ui@latest add select dialog alert-dialog
npx shadcn-ui@latest add dropdown-menu pagination

# Alternativa: Mantine (biblioteca completa)
npm install @mantine/core @mantine/hooks @mantine/dates
```

### Gráficos e Visualizações:
```bash
# Para dashboards e métricas
npm install recharts
# ou
npm install chart.js react-chartjs-2
```

### Mapas:
```bash
# Para sistema de entregas
npm install react-leaflet leaflet
npm install @types/leaflet
```

### Tabelas Avançadas:
```bash
# Para tabelas complexas com filtros/paginação
npm install @tanstack/react-table
```

## 🏗️ Estrutura do Projeto

```
src/
├── app/                    # Next.js App Router
│   ├── dashboard/         # Dashboard principal
│   ├── pedidos/          # Gestão de pedidos
│   ├── entregadores/     # Gestão de entregadores
│   ├── cardapio/         # Gestão do menu
│   ├── clientes/         # Gestão de clientes
│   └── layout.tsx        # Layout global
├── components/            # Componentes reutilizáveis
│   ├── ui/               # Componentes base (shadcn/ui)
│   ├── charts/           # Componentes de gráficos
│   ├── tables/           # Componentes de tabelas
│   └── maps/             # Componentes de mapas
├── hooks/                # Custom hooks
├── services/             # Integração com API
├── types/                # TypeScript types
├── lib/                  # Utilitários e configurações
└── utils/                # Funções auxiliares
```

## 🔌 Integração com API Spring Boot

### Configuração Base do Axios:

```typescript
// lib/api.ts
import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1',
  timeout: 10000,
});

// Interceptors para auth e error handling
api.interceptors.request.use((config) => {
  // Adicionar token de autenticação quando implementado
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Global error handling
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default api;
```

### Tipos TypeScript baseados na API:

```typescript
// types/api.ts

// Enums baseados no backend
export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  IN_PREPARATION = 'IN_PREPARATION',
  READY = 'READY',
  OUT_FOR_DELIVERY = 'OUT_FOR_DELIVERY',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export enum OrderType {
  DELIVERY = 'DELIVERY',
  PICKUP = 'PICKUP',
  DINE_IN = 'DINE_IN'
}

export enum CustomerStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  BLOCKED = 'BLOCKED'
}

// DTOs baseados nos endpoints otimizados
export interface CustomerSummaryDTO {
  id: number;
  name: string;
  email: string;
  phone: string;
  cpf: string;
  loyaltyPoints: number;
  status: CustomerStatus;
  registrationDate: string;
}

export interface CustomerDTO extends CustomerSummaryDTO {
  addresses: AddressDTO[];
}

export interface PizzaSummaryDTO {
  id: number;
  name: string;
  description: string;
  category: string;
  priceSmall: number;
  priceMedium: number;
  priceLarge: number;
  priceExtraLarge?: number;
  available: boolean;
  imageUrl?: string;
  preparationTimeMinutes?: number;
}

export interface PizzaDTO extends PizzaSummaryDTO {
  ingredients: IngredientDTO[];
}

export interface OrderSummaryDTO {
  id: number;
  customerId: number;
  customerName: string;
  type: OrderType;
  status: OrderStatus;
  orderDateTime: string;
  estimatedDateTime?: string;
  total: number;
  deliveryFee?: number;
  itemCount: number;
}

export interface OrderDTO extends OrderSummaryDTO {
  customer: CustomerDTO;
  address?: AddressDTO;
  items: OrderItemDTO[];
  delivery?: DeliveryDTO;
  observations?: string;
}

export interface AddressDTO {
  id?: number;
  customerId?: number;
  street: string;
  number: string;
  complement?: string;
  neighborhood: string;
  city: string;
  state: string;
  zipCode: string;
  referencePoints?: string;
  type: 'RESIDENTIAL' | 'COMMERCIAL' | 'OTHER';
  deliveryFee?: number;
  primary: boolean;
}
```

### Services para consumo da API:

```typescript
// services/customerService.ts
import api from '@/lib/api';
import { CustomerSummaryDTO, CustomerDTO, AddressDTO } from '@/types/api';

export const customerService = {
  // Lista resumida (otimizada)
  getAll: () => 
    api.get<CustomerSummaryDTO[]>('/clientes'),
  
  // Detalhes completos
  getById: (id: number) => 
    api.get<CustomerDTO>(`/clientes/${id}`),
  
  // Busca por nome
  searchByName: (name: string) => 
    api.get<CustomerSummaryDTO[]>(`/clientes/search?name=${name}`),
  
  // Criar cliente
  create: (customer: Omit<CustomerDTO, 'id' | 'registrationDate' | 'addresses'>) =>
    api.post<CustomerDTO>('/clientes', customer),
  
  // Adicionar endereço
  addAddress: (customerId: number, address: Omit<AddressDTO, 'id' | 'customerId'>) =>
    api.post<AddressDTO>(`/clientes/${customerId}/enderecos`, address),
};

// services/orderService.ts
export const orderService = {
  // Lista resumida (otimizada) 
  getAll: () => 
    api.get<OrderSummaryDTO[]>('/pedidos'),
  
  // Detalhes completos
  getById: (id: number) => 
    api.get<OrderDTO>(`/pedidos/${id}`),
  
  // Por status
  getByStatus: (status: OrderStatus) => 
    api.get<OrderSummaryDTO[]>(`/pedidos/status/${status}`),
  
  // Pedidos de hoje
  getToday: () => 
    api.get<OrderSummaryDTO[]>('/pedidos/today'),
  
  // Atualizar status
  updateStatus: (id: number, status: OrderStatus) =>
    api.patch<OrderDTO>(`/pedidos/${id}/status?status=${status}`),
  
  // Criar pedido
  create: (order: Omit<OrderDTO, 'id' | 'orderDateTime'>) =>
    api.post<OrderDTO>('/pedidos', order),
};

// services/menuService.ts
export const menuService = {
  // Lista resumida (otimizada)
  getAllPizzas: () => 
    api.get<PizzaSummaryDTO[]>('/cardapio/pizzas'),
  
  // Detalhes completos com ingredientes
  getPizzaById: (id: number) => 
    api.get<PizzaDTO>(`/cardapio/pizzas/${id}`),
  
  // Por categoria
  getPizzasByCategory: (category: string) => 
    api.get<PizzaSummaryDTO[]>(`/cardapio/pizzas/category/${category}`),
  
  // Busca
  searchPizzas: (name: string) => 
    api.get<PizzaSummaryDTO[]>(`/cardapio/pizzas/search?name=${name}`),
};
```

### React Query Setup:

```typescript
// lib/queryClient.ts
import { QueryClient } from '@tanstack/react-query';

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
      staleTime: 5 * 60 * 1000, // 5 minutos
    },
  },
});

// hooks/useOrders.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { orderService } from '@/services/orderService';

export const useOrders = () => {
  return useQuery({
    queryKey: ['orders'],
    queryFn: () => orderService.getAll(),
  });
};

export const useOrder = (id: number) => {
  return useQuery({
    queryKey: ['orders', id],
    queryFn: () => orderService.getById(id),
    enabled: !!id,
  });
};

export const useUpdateOrderStatus = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ id, status }: { id: number; status: OrderStatus }) =>
      orderService.updateStatus(id, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['orders'] });
    },
  });
};
```

## 📊 Componentes Principais

### Dashboard Components:

```typescript
// components/dashboard/MetricsCards.tsx
interface MetricsData {
  totalOrders: number;
  ordersInProgress: number;
  outForDelivery: number;
  averageDeliveryTime: number;
}

export function MetricsCards({ data }: { data: MetricsData }) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <Card>
        <CardContent className="p-6">
          <div className="flex flex-col gap-2">
            <p className="text-sm font-medium">Total Orders</p>
            <p className="text-2xl font-bold">{data.totalOrders}</p>
          </div>
        </CardContent>
      </Card>
      {/* Repetir para outras métricas */}
    </div>
  );
}

// components/dashboard/OrdersChart.tsx
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

export function OrdersChart({ data }: { data: Array<{ day: string; orders: number }> }) {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <BarChart data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="day" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="orders" fill="#3b82f6" />
      </BarChart>
    </ResponsiveContainer>
  );
}
```

### Table Components:

```typescript
// components/orders/OrdersTable.tsx
import { useOrders } from '@/hooks/useOrders';
import { DataTable } from '@/components/ui/data-table';

const columns = [
  {
    id: 'id',
    header: 'ID',
    accessorKey: 'id',
  },
  {
    id: 'customerName',
    header: 'Cliente',
    accessorKey: 'customerName',
  },
  {
    id: 'status',
    header: 'Status',
    accessorKey: 'status',
    cell: ({ row }) => <OrderStatusBadge status={row.original.status} />,
  },
  {
    id: 'total',
    header: 'Total',
    accessorKey: 'total',
    cell: ({ row }) => formatCurrency(row.original.total),
  },
  {
    id: 'actions',
    header: 'Ações',
    cell: ({ row }) => <OrderActions order={row.original} />,
  },
];

export function OrdersTable() {
  const { data: orders, isLoading } = useOrders();
  
  if (isLoading) return <div>Carregando...</div>;
  
  return <DataTable columns={columns} data={orders || []} />;
}
```

### Form Components:

```typescript
// components/orders/CreateOrderForm.tsx
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';

const orderSchema = yup.object({
  customerId: yup.number().required(),
  type: yup.string().oneOf(['DELIVERY', 'PICKUP', 'DINE_IN']).required(),
  items: yup.array().min(1).required(),
});

export function CreateOrderForm({ onSubmit }: { onSubmit: (data: any) => void }) {
  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: yupResolver(orderSchema),
  });
  
  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {/* Form fields */}
    </form>
  );
}
```

## 🗺️ Integração com Mapas

```typescript
// components/maps/DeliveryMap.tsx
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

interface DeliveryLocation {
  id: number;
  address: string;
  lat: number;
  lng: number;
  status: string;
}

export function DeliveryMap({ deliveries }: { deliveries: DeliveryLocation[] }) {
  return (
    <MapContainer center={[-23.5505, -46.6333]} zoom={13} className="h-96 w-full">
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      />
      {deliveries.map((delivery) => (
        <Marker key={delivery.id} position={[delivery.lat, delivery.lng]}>
          <Popup>
            <div>
              <h3>{delivery.address}</h3>
              <p>Status: {delivery.status}</p>
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}
```

## 🎨 Implementação dos Protótipos

### Dashboard Principal:
- **MetricsCards** - Cards com totais e métricas
- **OrdersChart** - Gráfico de barras (pedidos por dia)
- **DeliveryTimeChart** - Gráfico de linha (tempo de entrega)
- **RecentOrders** - Lista dos pedidos mais recentes

### Gestão de Pedidos:
- **OrdersTable** - Tabela principal com filtros e paginação
- **OrderDetail** - Modal/página com detalhes completos
- **OrderStatusUpdater** - Controles para atualizar status
- **OrderForm** - Formulário para novos pedidos

### Gestão de Entregadores:
- **DriversTable** - Lista com status e localização
- **DriverRoute** - Visualização da rota no mapa
- **DriverAssignment** - Atribuição de pedidos

## 🔧 Configurações e Utilitários

### Formatação e Helpers:

```typescript
// utils/formatters.ts
export const formatCurrency = (value: number) => 
  new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);

export const formatDateTime = (date: string) =>
  new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(date));

// utils/orderStatus.ts
export const getStatusColor = (status: OrderStatus) => {
  const colors = {
    PENDING: 'bg-yellow-100 text-yellow-800',
    CONFIRMED: 'bg-blue-100 text-blue-800',
    IN_PREPARATION: 'bg-orange-100 text-orange-800',
    READY: 'bg-green-100 text-green-800',
    OUT_FOR_DELIVERY: 'bg-purple-100 text-purple-800',
    DELIVERED: 'bg-gray-100 text-gray-800',
    CANCELLED: 'bg-red-100 text-red-800',
  };
  return colors[status] || '';
};
```

### Variáveis de Ambiente:

```env
# .env.local
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
NEXT_PUBLIC_MAPS_API_KEY=your_maps_key_here
```

## 🚀 Comandos de Desenvolvimento

```bash
# Desenvolvimento
npm run dev

# Build
npm run build

# Lint
npm run lint

# Type check
npm run type-check
```

## 📱 Responsividade

O design deve ser totalmente responsivo seguindo os breakpoints do Tailwind:
- **sm**: 640px+ (tablets)
- **md**: 768px+ (tablets landscape)
- **lg**: 1024px+ (desktop)
- **xl**: 1280px+ (desktop large)

## 🔐 Autenticação (Futuro)

Quando implementar autenticação:
- JWT tokens no localStorage/cookies
- Interceptors do Axios para refresh automático
- Protected routes com Next.js middleware
- Context/Provider para estado de auth

## 📈 Performance

- **React Query** para cache inteligente
- **Next.js Image** para otimização de imagens
- **Lazy loading** para componentes pesados
- **Virtualization** para tabelas grandes

Esta documentação serve como base completa para desenvolvimento do frontend React, garantindo integração eficiente com a API Spring Boot otimizada.