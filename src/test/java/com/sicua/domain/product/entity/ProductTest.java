package com.sicua.domain.product.entity;

import com.sicua.domain.product.valueobject.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void constructor_ValidData_CreatesProduct() {
        // Arrange
        ProductId productId = ProductId.generate();
        String storeId = "test-store";
        String name = "Test Product";
        String brand = "Test Brand";
        String category = "Electronics";
        BigDecimal price = new BigDecimal("99.99");
        Integer quantity = 10;

        // Act
        Product product = new Product(productId, storeId, name, brand, category, price, quantity);

        // Assert
        assertEquals(productId, product.getProductId());
        assertEquals(storeId, product.getStoreId());
        assertEquals(name, product.getName());
        assertEquals(brand, product.getBrand());
        assertEquals(category, product.getCategory());
        assertEquals(price, product.getPrice());
        assertEquals(quantity, product.getQuantity());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void constructor_NullProductId_ThrowsException() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new Product(null, "test-store", "Test Product", "Test Brand", "Electronics", new BigDecimal("99.99"), 10);
        });
    }

    @Test
    void constructor_NegativePrice_ThrowsException() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Product(ProductId.generate(), "test-store", "Test Product", "Test Brand", "Electronics", new BigDecimal("-10.00"), 10);
        });
    }

    @Test
    void constructor_NegativeQuantity_ThrowsException() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Product(ProductId.generate(), "test-store", "Test Product", "Test Brand", "Electronics", new BigDecimal("99.99"), -5);
        });
    }

    @Test
    void reduceStock_ValidQuantity_ReducesStock() {
        // Arrange
        Product product = new Product(
                ProductId.generate(),
                "test-store",
                "Test Product",
                "Test Brand",
                "Electronics",
                new BigDecimal("99.99"),
                10
        );

        // Act
        product.reduceStock(5);

        // Assert
        assertEquals(5, product.getQuantity());
    }

    @Test
    void reduceStock_InsufficientStock_ThrowsException() {
        // Arrange
        Product product = new Product(
                ProductId.generate(),
                "test-store",
                "Test Product",
                "Test Brand",
                "Electronics",
                new BigDecimal("99.99"),
                5
        );

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            product.reduceStock(10);
        });
    }

    @Test
    void hasEnoughStock_SufficientStock_ReturnsTrue() {
        // Arrange
        Product product = new Product(
                ProductId.generate(),
                "test-store",
                "Test Product",
                "Test Brand",
                "Electronics",
                new BigDecimal("99.99"),
                10
        );

        // Act & Assert
        assertTrue(product.hasEnoughStock(5));
        assertTrue(product.hasEnoughStock(10));
    }

    @Test
    void hasEnoughStock_InsufficientStock_ReturnsFalse() {
        // Arrange
        Product product = new Product(
                ProductId.generate(),
                "test-store",
                "Test Product",
                "Test Brand",
                "Electronics",
                new BigDecimal("99.99"),
                5
        );

        // Act & Assert
        assertFalse(product.hasEnoughStock(10));
    }

    @Test
    void updateProduct_ValidData_UpdatesProduct() {
        // Arrange
        Product product = new Product(
                ProductId.generate(),
                "test-store",
                "Original Product",
                "Original Brand",
                "Original Category",
                new BigDecimal("50.00"),
                5
        );

        // Act
        product.updateProduct("Updated Product", "Updated Brand", "Updated Category", new BigDecimal("75.00"), 15);

        // Assert
        assertEquals("Updated Product", product.getName());
        assertEquals("Updated Brand", product.getBrand());
        assertEquals("Updated Category", product.getCategory());
        assertEquals(new BigDecimal("75.00"), product.getPrice());
        assertEquals(15, product.getQuantity());
    }
}
