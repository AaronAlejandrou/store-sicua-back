package com.sicua.domain.sale.service;

import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.valueobject.ProductId;
import com.sicua.domain.sale.entity.Sale;
import com.sicua.domain.sale.entity.SaleItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Domain service for Sale business logic
 */
@Service
public class SaleDomainService {
    
    private final ProductRepository productRepository;
    
    public SaleDomainService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    /**
     * Validates stock availability for all items in a sale
     * @param sale the sale to validate
     * @return true if all items have enough stock
     */
    public boolean validateStockAvailability(Sale sale) {
        Map<ProductId, Integer> requiredQuantities = sale.getItems().stream()
                .collect(Collectors.groupingBy(
                        SaleItem::getProductId,
                        Collectors.summingInt(SaleItem::getQuantity)
                ));
        
        for (Map.Entry<ProductId, Integer> entry : requiredQuantities.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + entry.getKey()));
            
            if (!product.hasEnoughStock(entry.getValue())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Reduces stock for all products in the sale
     * @param sale the sale containing items to reduce stock for
     */
    public void reduceStockForSale(Sale sale) {
        Map<ProductId, Integer> quantitiesToReduce = sale.getItems().stream()
                .collect(Collectors.groupingBy(
                        SaleItem::getProductId,
                        Collectors.summingInt(SaleItem::getQuantity)
                ));
        
        for (Map.Entry<ProductId, Integer> entry : quantitiesToReduce.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + entry.getKey()));
            
            product.reduceStock(entry.getValue());
            productRepository.save(product);
        }
    }
    
    /**
     * Validates if a sale can be marked as invoiced
     * @param sale the sale to validate
     * @return true if the sale can be invoiced
     */
    public boolean canMarkAsInvoiced(Sale sale) {
        return !sale.getInvoiced() && !sale.getItems().isEmpty();
    }
}
