import React from 'react';
import { AuthProvider } from './components/AuthProvider';
import { AppRoutes } from './components/AppRoutes';
import './App.css';
import './styles/globals.css';

function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}

export default App;
