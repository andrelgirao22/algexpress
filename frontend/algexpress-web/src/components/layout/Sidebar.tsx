import React, { useState } from 'react';
import { User } from '../../features/auth/types/auth';
import '../../styles/Sidebar.css';

interface SidebarProps {
  user: User;
  onLogout: () => void;
}

export const Sidebar = ({ user, onLogout }: SidebarProps) => {
  const [activeItem, setActiveItem] = useState('/dashboard');

  const menuItems = [
    {
      title: 'Dashboard',
      icon: '📊',
      href: '/dashboard',
      description: 'Visão geral e métricas',
      color: 'var(--color-primary-500)'
    },
    {
      title: 'Pedidos',
      icon: '🍕',
      href: '/pedidos',
      description: 'Gestão de pedidos',
      color: 'var(--color-accent-500)'
    },
    {
      title: 'Cardápio',
      icon: '📋',
      href: '/cardapio',
      description: 'Gestão do menu',
      color: 'var(--color-secondary-500)'
    },
    {
      title: 'Clientes',
      icon: '👥',
      href: '/clientes',
      description: 'Gestão de clientes',
      color: 'var(--color-info)'
    },
    {
      title: 'Entregadores',
      icon: '🏍️',
      href: '/entregadores',
      description: 'Gestão de entregas',
      color: 'var(--color-warning)'
    },
    {
      title: 'Relatórios',
      icon: '📈',
      href: '/relatorios',
      description: 'Analytics e reports',
      color: 'var(--color-secondary-600)'
    }
  ];

  const handleItemClick = (href: string) => {
    setActiveItem(href);
    // Aqui você pode adicionar navegação real depois
  };

  return (
    <div className="sidebar">
      {/* Header com gradiente */}
      <div className="sidebar-header">
        <div className="sidebar-logo">
          <div className="logo-icon animate-bounce">🍕</div>
          <div className="logo-text">
            <h1 className="brand-title">AlgExpress</h1>
            <p className="brand-subtitle">Sistema de Gestão</p>
          </div>
        </div>
      </div>

      {/* User Info */}
      <div className="sidebar-user">
        <div className="user-card">
          <div className="user-avatar">
            <span className="user-initials">
              {user.name.split(' ').map(n => n[0]).join('').toUpperCase()}
            </span>
          </div>
          <div className="user-info">
            <p className="user-name">{user.name}</p>
            <div className="user-role-badge">
              <span className="role-icon">👨‍💼</span>
              <span className="role-text">{user.role}</span>
            </div>
          </div>
          <div className="user-status online"></div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="sidebar-nav">
        <div className="nav-section">
          <h3 className="nav-section-title">🏪 Gestão</h3>
          <ul className="nav-list">
            {menuItems.map((item) => (
              <li key={item.href} className="nav-item">
                <button
                  onClick={() => handleItemClick(item.href)}
                  className={`nav-link ${activeItem === item.href ? 'active' : ''}`}
                  style={{'--item-color': item.color} as React.CSSProperties}
                >
                  <div className="nav-icon-wrapper">
                    <span className="nav-icon">{item.icon}</span>
                  </div>
                  <div className="nav-content">
                    <span className="nav-title">{item.title}</span>
                    <span className="nav-description">{item.description}</span>
                  </div>
                  <div className="nav-indicator"></div>
                </button>
              </li>
            ))}
          </ul>
        </div>

        {/* Seção de configurações */}
        <div className="nav-section">
          <h3 className="nav-section-title">⚙️ Sistema</h3>
          <ul className="nav-list">
            <li className="nav-item">
              <button className="nav-link">
                <div className="nav-icon-wrapper">
                  <span className="nav-icon">⚙️</span>
                </div>
                <div className="nav-content">
                  <span className="nav-title">Configurações</span>
                  <span className="nav-description">Ajustes do sistema</span>
                </div>
              </button>
            </li>
            <li className="nav-item">
              <button className="nav-link">
                <div className="nav-icon-wrapper">
                  <span className="nav-icon">👤</span>
                </div>
                <div className="nav-content">
                  <span className="nav-title">Perfil</span>
                  <span className="nav-description">Minha conta</span>
                </div>
              </button>
            </li>
          </ul>
        </div>
      </nav>

      {/* Footer com logout */}
      <div className="sidebar-footer">
        <button
          onClick={onLogout}
          className="logout-button"
        >
          <span className="logout-icon">🚪</span>
          <span className="logout-text">Sair do Sistema</span>
          <div className="logout-indicator"></div>
        </button>
        
        <div className="footer-info">
          <p className="footer-version">v1.0.0</p>
          <p className="footer-copyright">© 2024 AlgExpress</p>
        </div>
      </div>
    </div>
  );
};