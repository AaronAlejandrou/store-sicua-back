-- Add brand column to products table
ALTER TABLE products ADD COLUMN brand VARCHAR(100);

-- Update existing products with empty brand if needed
UPDATE products SET brand = '' WHERE brand IS NULL;
