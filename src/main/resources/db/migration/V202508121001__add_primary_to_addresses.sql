-- Add is_primary field to addresses table
-- Migration: V202508121001__add_primary_to_addresses.sql

ALTER TABLE addresses 
ADD COLUMN is_primary BOOLEAN NOT NULL DEFAULT FALSE;

-- Add index for primary addresses (useful for queries)
CREATE INDEX idx_addresses_primary ON addresses(customer_id, is_primary) WHERE is_primary = TRUE;

-- Add comment
COMMENT ON COLUMN addresses.is_primary IS 'Indicates if this is the customer primary/default address';