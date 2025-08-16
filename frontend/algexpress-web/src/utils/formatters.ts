// Funções utilitárias para formatação
// Padronização de exibição de dados na aplicação

// Formatar moeda em Real brasileiro
export const formatCurrency = (value: number): string => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
};

// Formatar data e hora
export const formatDateTime = (date: string): string => {
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(date));
};

// Formatar apenas data
export const formatDate = (date: string): string => {
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(new Date(date));
};

// Formatar telefone
export const formatPhone = (phone: string): string => {
  // Implementar formatação de telefone brasileiro
  return phone;
};

// Formatar CPF
export const formatCPF = (cpf: string): string => {
  // Implementar formatação de CPF
  return cpf;
};
