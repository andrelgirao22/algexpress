// Tipos TypeScript baseados na API Spring Boot
// Mantém consistência entre frontend e backend

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

// DTOs para transferência de dados
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
