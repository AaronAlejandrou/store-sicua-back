package com.sicua.application.excel.usecase;

import com.sicua.application.auth.SessionService;
import com.sicua.application.category.usecase.CategoryService;
import com.sicua.application.excel.dto.ExcelProductImportRequest;
import com.sicua.application.excel.service.ExcelProcessingService;
import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Use case for exporting all products to Excel format
 */
@Service
public class ExportProductsToExcelUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ExportProductsToExcelUseCase.class);
    
    private final ExcelProcessingService excelProcessingService;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SessionService sessionService;
    
    public ExportProductsToExcelUseCase(ExcelProcessingService excelProcessingService,
                                      ProductRepository productRepository,
                                      CategoryService categoryService,
                                      SessionService sessionService) {
        this.excelProcessingService = excelProcessingService;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.sessionService = sessionService;
    }
    
    @Transactional(readOnly = true)
    public byte[] execute() {
        logger.info("Starting export of all products to Excel");
        
        try {
            String storeId = sessionService.getCurrentStoreId();
            
            // Get all products for the store
            List<Product> products = productRepository.findAllByStoreId(storeId);
            logger.info("Found {} products to export for store: {}", products.size(), storeId);
            
            if (products.isEmpty()) {
                logger.warn("No products found to export for store: {}", storeId);
                // Return empty Excel with headers only
                return excelProcessingService.exportProductsToExcel(new ArrayList<>());
            }
            
            // Get all categories for name lookup
            Map<Integer, String> categoryNames = categoryService.getAllCategories(storeId)
                .stream()
                .collect(Collectors.toMap(
                    cat -> cat.getCategoryNumber(),
                    cat -> cat.getName(),
                    (existing, replacement) -> existing // Keep first if duplicate
                ));
            
            // Convert products to Excel format
            List<ExcelProductImportRequest> excelProducts = products.stream()
                .map(product -> convertProductToExcelFormat(product, categoryNames))
                .collect(Collectors.toList());
            
            // Generate Excel file
            byte[] excelBytes = excelProcessingService.exportProductsToExcel(excelProducts);
            
            logger.info("Successfully exported {} products to Excel", excelProducts.size());
            return excelBytes;
            
        } catch (Exception e) {
            logger.error("Error exporting products to Excel: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to export products to Excel: " + e.getMessage(), e);
        }
    }
    
    private ExcelProductImportRequest convertProductToExcelFormat(Product product, 
                                                                Map<Integer, String> categoryNames) {
        // Parse size field to extract talla and color
        String[] sizeParts = parseSizeField(product.getSize());
        String talla = sizeParts[0];
        String color = sizeParts[1];
        
        // Get category name
        String categoryName = null;
        if (product.getCategoryNumber() != null) {
            categoryName = categoryNames.get(product.getCategoryNumber());
        }
        
        return new ExcelProductImportRequest(
            product.getProductId().getValue(), // Product ID first
            product.getName(),
            product.getPrice(), 
            talla,
            color,
            product.getCategoryNumber(),
            categoryName,
            product.getQuantity(),
            product.getBrand()
        );
    }
    
    /**
     * Parse size field that might contain "talla - color" format
     * Returns [talla, color] or [null, null] if not parseable
     */
    private String[] parseSizeField(String size) {
        if (size == null || size.trim().isEmpty()) {
            return new String[]{null, null};
        }
        
        // Try to split by " - " separator
        String[] parts = size.split(" - ");
        if (parts.length == 2) {
            return new String[]{parts[0].trim(), parts[1].trim()};
        } else if (parts.length == 1) {
            // Only one part, assume it's talla
            return new String[]{parts[0].trim(), null};
        } else {
            // Complex format, put everything in talla
            return new String[]{size.trim(), null};
        }
    }
}
