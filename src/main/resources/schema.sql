-- Database initialization script for SICUA system - PostgreSQL/Supabase
-- Note: CREATE DATABASE is not needed for Supabase as it provides the database

-- Store configuration table (serves as user table)
CREATE TABLE IF NOT EXISTS store_config (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for store_config
CREATE INDEX IF NOT EXISTS idx_store_email ON store_config(email);

-- Categories table (linked to store owner)
CREATE TABLE IF NOT EXISTS categories (
    category_id VARCHAR(36) PRIMARY KEY,
    store_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    category_number INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for categories
CREATE INDEX IF NOT EXISTS idx_category_store_id ON categories(store_id);
CREATE INDEX IF NOT EXISTS idx_category_number ON categories(category_number);
CREATE INDEX IF NOT EXISTS idx_category_name ON categories(name);
CREATE UNIQUE INDEX IF NOT EXISTS uk_category_store_number ON categories(store_id, category_number);
CREATE UNIQUE INDEX IF NOT EXISTS uk_category_store_name ON categories(store_id, name);

-- Add foreign key constraint for categories
ALTER TABLE categories 
ADD CONSTRAINT fk_category_store 
FOREIGN KEY (store_id) REFERENCES store_config(id) ON DELETE CASCADE;

-- Products table (linked to store owner)
CREATE TABLE IF NOT EXISTS products (
    product_id VARCHAR(36) PRIMARY KEY,
    store_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    category_number INTEGER,
    size VARCHAR(50),
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for products
CREATE INDEX IF NOT EXISTS idx_product_store_id ON products(store_id);
CREATE INDEX IF NOT EXISTS idx_product_name ON products(name);
CREATE INDEX IF NOT EXISTS idx_product_category_number ON products(category_number);
CREATE INDEX IF NOT EXISTS idx_product_size ON products(size);

-- Add foreign key constraint for products
ALTER TABLE products 
ADD CONSTRAINT fk_product_store 
FOREIGN KEY (store_id) REFERENCES store_config(id) ON DELETE CASCADE;

-- Sales table (linked to store owner)
CREATE TABLE IF NOT EXISTS sales (
    id VARCHAR(36) PRIMARY KEY,
    store_id VARCHAR(36) NOT NULL,
    client_dni VARCHAR(20),
    client_name VARCHAR(255),
    date TIMESTAMP NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    invoiced BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for sales
CREATE INDEX IF NOT EXISTS idx_sale_store_id ON sales(store_id);
CREATE INDEX IF NOT EXISTS idx_sale_date ON sales(date);
CREATE INDEX IF NOT EXISTS idx_sale_client_dni ON sales(client_dni);
CREATE INDEX IF NOT EXISTS idx_sale_invoiced ON sales(invoiced);

-- Add foreign key constraint for sales
ALTER TABLE sales 
ADD CONSTRAINT fk_sale_store 
FOREIGN KEY (store_id) REFERENCES store_config(id) ON DELETE CASCADE;

-- Sale items table (linked to sales)
CREATE TABLE IF NOT EXISTS sale_items (
    id BIGSERIAL PRIMARY KEY,
    sale_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL
);

-- Create indexes for sale_items
CREATE INDEX IF NOT EXISTS idx_sale_item_sale_id ON sale_items(sale_id);
CREATE INDEX IF NOT EXISTS idx_sale_item_product_id ON sale_items(product_id);

-- Add foreign key constraint for sale_items
ALTER TABLE sale_items 
ADD CONSTRAINT fk_sale_item_sale 
FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE;

-- Create triggers for updated_at columns (PostgreSQL equivalent of MySQL's ON UPDATE CURRENT_TIMESTAMP)
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply triggers to tables that need auto-updated timestamps
CREATE TRIGGER update_store_config_updated_at BEFORE UPDATE ON store_config FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_categories_updated_at BEFORE UPDATE ON categories FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
