import React from 'react';
import '../../../styles/DashboardHome.css';

interface MetricCardProps {
  title: string;
  value: string | number;
  icon: string;
  color: string;
  description?: string;
  trend?: 'up' | 'down' | 'neutral';
}

const MetricCard = ({ title, value, icon, color, description, trend = 'neutral' }: MetricCardProps) => (
  <div className="metric-card animate-fade-in">
    <div className="metric-header">
      <div className="metric-info">
        <p className="metric-title">{title}</p>
        <p className="metric-value" style={{ color }}>{value}</p>
        {description && (
          <div className="metric-description">
            <span className={`trend-indicator ${trend}`}>
              {trend === 'up' && '📈'}
              {trend === 'down' && '📉'}
              {trend === 'neutral' && '📊'}
            </span>
            <span className="metric-desc-text">{description}</span>
          </div>
        )}
      </div>
      <div className="metric-icon-wrapper">
        <span className="metric-icon">{icon}</span>
      </div>
    </div>
    <div className="metric-footer">
      <div className="metric-bar">
        <div className="metric-progress" style={{ backgroundColor: color, width: '70%' }}></div>
      </div>
    </div>
  </div>
);

interface QuickActionProps {
  title: string;
  description: string;
  icon: string;
  href: string;
  color: string;
  gradient: string;
}

const QuickAction = ({ title, description, icon, href, color, gradient }: QuickActionProps) => (
  <button 
    className="quick-action-card"
    onClick={() => {
      // Aqui você pode adicionar navegação real depois
      console.log(`Navegando para: ${href}`);
    }}
  >
    <div className="action-icon-wrapper" style={{ background: gradient }}>
      <span className="action-icon">{icon}</span>
    </div>
    <div className="action-content">
      <h3 className="action-title">{title}</h3>
      <p className="action-description">{description}</p>
    </div>
    <div className="action-arrow">→</div>
  </button>
);

export const DashboardHome = () => {
  const metrics = [
    {
      title: 'Pedidos Hoje',
      value: 42,
      icon: '🍕',
      color: 'var(--color-primary-600)',
      description: '+12% vs ontem',
      trend: 'up' as const
    },
    {
      title: 'Em Preparo',
      value: 8,
      icon: '👨‍🍳',
      color: 'var(--color-warning)',
      description: 'Aguardando preparo',
      trend: 'neutral' as const
    },
    {
      title: 'Saiu p/ Entrega',
      value: 5,
      icon: '🏍️',
      color: 'var(--color-info)',
      description: 'Em rota de entrega',
      trend: 'up' as const
    },
    {
      title: 'Faturamento',
      value: 'R$ 1.847',
      icon: '💰',
      color: 'var(--color-success)',
      description: 'Hoje até agora',
      trend: 'up' as const
    }
  ];

  const quickActions = [
    {
      title: 'Novo Pedido',
      description: 'Criar pedido manualmente',
      icon: '➕',
      href: '/pedidos/novo',
      color: 'var(--color-primary-500)',
      gradient: 'linear-gradient(135deg, var(--color-primary-500), var(--color-primary-600))'
    },
    {
      title: 'Ver Pedidos',
      description: 'Gerenciar todos os pedidos',
      icon: '📋',
      href: '/pedidos',
      color: 'var(--color-secondary-500)',
      gradient: 'linear-gradient(135deg, var(--color-secondary-500), var(--color-secondary-600))'
    },
    {
      title: 'Cardápio',
      description: 'Gerenciar pizzas e preços',
      icon: '🍕',
      href: '/cardapio',
      color: 'var(--color-accent-500)',
      gradient: 'linear-gradient(135deg, var(--color-accent-500), var(--color-accent-600))'
    },
    {
      title: 'Clientes',
      description: 'Visualizar base de clientes',
      icon: '👥',
      href: '/clientes',
      color: 'var(--color-info)',
      gradient: 'linear-gradient(135deg, var(--color-info), #2563eb)'
    }
  ];

  return (
    <div className="dashboard-container">
      {/* Header */}
      <div className="dashboard-header">
        <div className="header-content">
          <h1 className="dashboard-title">
            <span className="title-icon">📊</span>
            Dashboard AlgExpress
          </h1>
          <p className="dashboard-subtitle">Visão geral das operações da pizzaria</p>
        </div>
        <div className="header-actions">
          <div className="time-display">
            <span className="time-icon">🕐</span>
            <span className="time-text">{new Date().toLocaleTimeString('pt-BR', { 
              hour: '2-digit', 
              minute: '2-digit' 
            })}</span>
          </div>
        </div>
      </div>

      {/* Metrics Grid */}
      <div className="metrics-section">
        <h2 className="section-title">📈 Métricas em Tempo Real</h2>
        <div className="metrics-grid">
          {metrics.map((metric, index) => (
            <MetricCard key={index} {...metric} />
          ))}
        </div>
      </div>

      {/* Quick Actions */}
      <div className="actions-section">
        <h2 className="section-title">⚡ Ações Rápidas</h2>
        <div className="actions-grid">
          {quickActions.map((action, index) => (
            <QuickAction key={index} {...action} />
          ))}
        </div>
      </div>

      {/* Recent Orders Preview */}
      <div className="orders-section">
        <h2 className="section-title">🍕 Pedidos Recentes</h2>
        <div className="orders-card">
          <div className="orders-list">
            {[
              { id: 101, pizza: 'Pizza Margherita (G)', customer: 'João Silva', price: 'R$ 32,90', status: 'Em preparo', time: '15 min', statusColor: 'var(--color-warning)' },
              { id: 102, pizza: 'Pizza Calabresa (M)', customer: 'Maria Santos', price: 'R$ 28,90', status: 'Saiu p/ entrega', time: '8 min', statusColor: 'var(--color-info)' },
              { id: 103, pizza: 'Pizza Portuguesa (G)', customer: 'Pedro Costa', price: 'R$ 35,90', status: 'Entregue', time: '2 min', statusColor: 'var(--color-success)' }
            ].map((order) => (
              <div key={order.id} className="order-item">
                <div className="order-info">
                  <div className="order-badge">
                    <span className="order-id">#{order.id}</span>
                  </div>
                  <div className="order-details">
                    <p className="order-pizza">{order.pizza}</p>
                    <p className="order-customer">
                      <span className="customer-icon">👤</span>
                      {order.customer} • {order.price}
                    </p>
                  </div>
                </div>
                <div className="order-status">
                  <span 
                    className="status-badge" 
                    style={{ 
                      backgroundColor: `${order.statusColor}20`,
                      color: order.statusColor,
                      borderColor: `${order.statusColor}40`
                    }}
                  >
                    {order.status}
                  </span>
                  <p className="order-time">
                    <span className="time-icon">⏱️</span>
                    há {order.time}
                  </p>
                </div>
              </div>
            ))}
          </div>
          <div className="orders-footer">
            <button className="view-all-btn">
              <span className="btn-icon">👀</span>
              Ver todos os pedidos
              <span className="btn-arrow">→</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};