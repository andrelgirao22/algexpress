import React from 'react';
import { useAuth } from '../hooks/useAuth';
import { LoginForm } from '../features/auth/components/LoginForm';
import { DashboardLayout } from './layout/DashboardLayout';
import { DashboardHome } from '../features/dashboard/components/DashboardHome';

export const AppRoutes = () => {
  const { authState, login, logout } = useAuth();

  // Loading state
  if (authState.loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Carregando...</p>
        </div>
      </div>
    );
  }

  // Not authenticated - show login
  if (!authState.isAuthenticated) {
    return <LoginForm onLogin={login} loading={authState.loading} />;
  }

  // Authenticated - show dashboard
  return (
    <DashboardLayout user={authState.user!} onLogout={logout}>
      {/* Por enquanto só o dashboard home, depois implementar roteamento completo */}
      <DashboardHome />
    </DashboardLayout>
  );
};