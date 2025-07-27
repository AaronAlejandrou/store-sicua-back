-- Database initialization script for SICUA system
CREATE DATABASE IF NOT EXISTS sicua_db;

USE sicua_db;

-- Products table
CREATE TABLE IF NOT EXISTS products (
    product_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_name (name),
    INDEX idx_product_category (category)
);

-- Store configuration table
CREATE TABLE IF NOT EXISTS store_config (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    email VARCHAR(255),
    phone VARCHAR(50),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Sales table
CREATE TABLE IF NOT EXISTS sales (
    id VARCHAR(36) PRIMARY KEY,
    client_dni VARCHAR(20),
    client_name VARCHAR(255),
    date TIMESTAMP NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    invoiced BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_sale_date (date),
    INDEX idx_sale_client_dni (client_dni),
    INDEX idx_sale_invoiced (invoiced)
);

-- Sale items table
CREATE TABLE IF NOT EXISTS sale_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE,
    INDEX idx_sale_item_sale_id (sale_id),
    INDEX idx_sale_item_product_id (product_id)
);
