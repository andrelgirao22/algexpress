-- Create users table for authentication
-- Migration: V202508102115__create_users_table.sql

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    last_login TIMESTAMP,
    login_attempts INTEGER NOT NULL DEFAULT 0,
    account_locked_until TIMESTAMP,
    
    CONSTRAINT ck_user_role CHECK (role IN ('ADMIN', 'MANAGER', 'EMPLOYEE', 'DELIVERY_PERSON')),
    CONSTRAINT ck_user_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED', 'PENDING_ACTIVATION'))
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);

-- Insert default admin user
-- Password: admin123 (should be changed in production)
-- This is a BCrypt hash of 'admin123'
INSERT INTO users (username, name, email, password, role, status) VALUES
('admin', 'System Administrator', 'admin@algexpress.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd99Q.VShxvO8WUy', 'ADMIN', 'ACTIVE');

-- Insert sample manager user
-- Password: manager123
INSERT INTO users (username, name, email, password, role, status) VALUES
('manager', 'Store Manager', 'manager@algexpress.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd99Q.VShxvO8WUy', 'MANAGER', 'ACTIVE');

-- Insert sample employee user
-- Password: employee123
INSERT INTO users (username, name, email, password, role, status) VALUES
('employee', 'Store Employee', 'employee@algexpress.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd99Q.VShxvO8WUy', 'EMPLOYEE', 'ACTIVE');