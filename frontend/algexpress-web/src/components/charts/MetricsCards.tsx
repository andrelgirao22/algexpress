// Cards de métricas para o dashboard
// Exibe indicadores principais (pedidos totais, em andamento, etc.)
export interface MetricsCardsProps {
  totalOrders?: number;
  ordersInProgress?: number;
  outForDelivery?: number;
  averageDeliveryTime?: number;
}

export const MetricsCards = (props: MetricsCardsProps) => {
  // Implementação dos cards de métricas
  return null;
};
