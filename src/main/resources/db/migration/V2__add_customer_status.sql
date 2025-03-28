-- Add status column to customers table
ALTER TABLE customers ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL;

-- Create index on status for faster filtering
CREATE INDEX idx_customers_status ON customers(status);

-- Add comments to explain the status column
COMMENT ON COLUMN customers.status IS 'Customer status: ACTIVE, INACTIVE, SUSPENDED';