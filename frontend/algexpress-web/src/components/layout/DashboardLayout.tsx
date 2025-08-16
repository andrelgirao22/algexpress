import React from 'react';
import { Sidebar } from './Sidebar';
import { User } from '../../features/auth/types/auth';

interface DashboardLayoutProps {
  user: User;
  onLogout: () => void;
  children: React.ReactNode;
}

export const DashboardLayout = ({ user, onLogout, children }: DashboardLayoutProps) => {
  return (
    <div style={{ 
      display: 'flex', 
      height: '100vh', 
      background: 'var(--color-background)' 
    }}>
      {/* Sidebar */}
      <Sidebar user={user} onLogout={onLogout} />
      
      {/* Main Content */}
      <div style={{ 
        flex: 1, 
        display: 'flex', 
        flexDirection: 'column', 
        overflow: 'hidden' 
      }}>
        {/* Header */}
        <header style={{
          background: 'var(--color-surface)',
          boxShadow: 'var(--shadow-sm)',
          borderBottom: `1px solid var(--color-border)`,
          padding: 'var(--spacing-lg) var(--spacing-xl)'
        }}>
          <div style={{ 
            display: 'flex', 
            alignItems: 'center', 
            justifyContent: 'space-between' 
          }}>
            <h2 style={{
              fontSize: 'var(--font-size-xl)',
              fontWeight: 'var(--font-weight-semibold)',
              color: 'var(--color-text-primary)',
              margin: 0,
              display: 'flex',
              alignItems: 'center',
              gap: 'var(--spacing-sm)'
            }}>
              🍕 Dashboard - AlgExpress
            </h2>
            <div style={{ 
              display: 'flex', 
              alignItems: 'center', 
              gap: 'var(--spacing-md)' 
            }}>
              <span style={{
                fontSize: 'var(--font-size-sm)',
                color: 'var(--color-text-secondary)'
              }}>
                Bem-vindo, {user.name}
              </span>
              <div style={{
                width: '32px',
                height: '32px',
                background: `linear-gradient(135deg, var(--color-primary-500), var(--color-primary-600))`,
                borderRadius: 'var(--radius-full)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                boxShadow: 'var(--shadow-md)'
              }}>
                <span style={{
                  fontSize: 'var(--font-size-xs)',
                  fontWeight: 'var(--font-weight-medium)',
                  color: 'var(--color-text-inverse)'
                }}>
                  {user.name.split(' ').map(n => n[0]).join('').toUpperCase()}
                </span>
              </div>
            </div>
          </div>
        </header>

        {/* Content Area */}
        <main style={{
          flex: 1,
          overflowY: 'auto',
          padding: 'var(--spacing-xl)'
        }}>
          {children}
        </main>
      </div>
    </div>
  );
};