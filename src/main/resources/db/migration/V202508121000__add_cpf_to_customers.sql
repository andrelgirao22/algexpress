-- Add CPF field to customers table
-- Migration: V202508121000__add_cpf_to_customers.sql

ALTER TABLE customers 
ADD COLUMN cpf VARCHAR(14);

-- Add index for CPF (optional but recommended for performance)
CREATE INDEX idx_customers_cpf ON customers(cpf);

-- Add comment
COMMENT ON COLUMN customers.cpf IS 'Customer Brazilian CPF document number in format xxx.xxx.xxx-xx';