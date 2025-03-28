-- Alter the id column type in customers table from SERIAL (INTEGER) to BIGSERIAL (BIGINT)

-- First, update the addresses table foreign key to avoid constraint violations
ALTER TABLE addresses DROP CONSTRAINT fk_customer;

-- Change the id column type in customers table
ALTER TABLE customers ALTER COLUMN id TYPE BIGINT;

-- Update the sequence to use bigint
ALTER SEQUENCE customers_id_seq AS BIGINT;

-- Change the customer_id column type in addresses table
ALTER TABLE addresses ALTER COLUMN customer_id TYPE BIGINT;

-- Recreate the foreign key constraint
ALTER TABLE addresses ADD CONSTRAINT fk_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(id)
    ON DELETE CASCADE;