-- Migration script to add authentication to existing store_config table
-- Add password and created_at columns
ALTER TABLE store_config 
ADD COLUMN password VARCHAR(255),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Make email unique and required (if not already)
ALTER TABLE store_config 
MODIFY COLUMN email VARCHAR(255) UNIQUE NOT NULL;

-- Add indexes for performance
CREATE INDEX idx_store_config_email ON store_config(email);

-- Update existing records to have created_at if null
UPDATE store_config 
SET created_at = updated_at 
WHERE created_at IS NULL;
