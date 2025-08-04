-- Database initialization script for SICUA system
CREATE DATABASE IF NOT EXISTS sicua_db;

USE sicua_db;

-- Store configuration table (serves as user table)
CREATE TABLE IF NOT EXISTS store_config (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_store_email (email)
);

-- Categories table (linked to store owner)
CREATE TABLE IF NOT EXISTS categories (
    category_id VARCHAR(36) PRIMARY KEY,
    store_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    category_number INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_store_id (store_id),
    INDEX idx_category_number (category_number),
    INDEX idx_category_name (name),
    UNIQUE KEY uk_category_store_number (store_id, category_number),
    UNIQUE KEY uk_category_store_name (store_id, name),
    FOREIGN KEY (store_id) REFERENCES store_config(id) ON DELETE CASCADE
);

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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_store_id (store_id),
    INDEX idx_product_name (name),
    INDEX idx_product_category_number (category_number),
    INDEX idx_product_size (size),
    FOREIGN KEY (store_id) REFERENCES store_config(id) ON DELETE CASCADE
);

-- Sales table (linked to store owner)
CREATE TABLE IF NOT EXISTS sales (
    id VARCHAR(36) PRIMARY KEY,
    store_id VARCHAR(36) NOT NULL,
    client_dni VARCHAR(20),
    client_name VARCHAR(255),
    date TIMESTAMP NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    invoiced BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_sale_store_id (store_id),
    INDEX idx_sale_date (date),
    INDEX idx_sale_client_dni (client_dni),
    INDEX idx_sale_invoiced (invoiced),
    FOREIGN KEY (store_id) REFERENCES store_config(id) ON DELETE CASCADE
);

-- Sale items table (linked to sales)
CREATE TABLE IF NOT EXISTS sale_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    INDEX idx_sale_item_sale_id (sale_id),
    INDEX idx_sale_item_product_id (product_id),
    FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE
);
