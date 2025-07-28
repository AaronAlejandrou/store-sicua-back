package com.sicua.application.product.usecase;

import com.sicua.application.auth.SessionService;
import com.sicua.application.product.dto.CreateProductRequest;
import com.sicua.application.product.dto.ProductResponse;
import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private SessionService sessionService;

    private CreateProductUseCase createProductUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createProductUseCase = new CreateProductUseCase(productRepository, sessionService);
    }

    @Test
    void execute_ValidRequest_ReturnsProductResponse() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
                "P001",
                "Test Product",
                "Test Brand",
                "Electronics",
                new BigDecimal("99.99"),
                10
        );

        when(sessionService.getCurrentStoreId()).thenReturn("test-store");

        Product savedProduct = new Product(
                ProductId.generate(),
                "test-store",
                "Test Product",
                "Test Brand",
                "Electronics",
                new BigDecimal("99.99"),
                10
        );

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        ProductResponse response = createProductUseCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("Test Product", response.getName());
        assertEquals("Electronics", response.getCategory());
        assertEquals(new BigDecimal("99.99"), response.getPrice());
        assertEquals(10, response.getQuantity());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void execute_RepositoryThrowsException_ThrowsRuntimeException() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
                "P002",
                "Test Product",
                "Test Brand",
                "Electronics",
                new BigDecimal("99.99"),
                10
        );

        when(sessionService.getCurrentStoreId()).thenReturn("test-store");
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createProductUseCase.execute(request);
        });

        assertTrue(exception.getMessage().contains("Failed to create product"));
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
