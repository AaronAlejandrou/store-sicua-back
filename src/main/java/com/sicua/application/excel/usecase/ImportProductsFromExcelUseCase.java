package com.sicua.application.excel.usecase;

import com.sicua.application.auth.SessionService;
import com.sicua.application.category.usecase.CategoryService;
import com.sicua.application.category.dto.CreateCategoryRequest;
import com.sicua.application.excel.dto.ExcelImportResponse;
import com.sicua.application.excel.dto.ExcelProductImportRequest;
import com.sicua.application.excel.service.ExcelProcessingService;
import com.sicua.domain.category.entity.Category;
import com.sicua.domain.product.entity.Product;
import com.sicua.domain.product.repository.ProductRepository;
import com.sicua.domain.product.valueobject.ProductId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.*;

/**
 * Use case for importing products from Excel with automatic category creation
 */
@Service
public class ImportProductsFromExcelUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ImportProductsFromExcelUseCase.class);
    
    private final ExcelProcessingService excelProcessingService;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final SessionService sessionService;
    private final Validator validator;
    
    public ImportProductsFromExcelUseCase(ExcelProcessingService excelProcessingService,
                                        CategoryService categoryService,
                                        ProductRepository productRepository,
                                        SessionService sessionService,
                                        Validator validator) {
        this.excelProcessingService = excelProcessingService;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.sessionService = sessionService;
        this.validator = validator;
    }
    
    @Transactional
    public ExcelImportResponse execute(MultipartFile file) {
        logger.info("Starting Excel import process for file: {}", file.getOriginalFilename());
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        int successfulImports = 0;
        int categoriesCreated = 0;
        int totalProcessed = 0;
        
        String storeId = sessionService.getCurrentStoreId();
        
        try {
            // Parse Excel file
            List<ExcelProductImportRequest> excelProducts = excelProcessingService.parseExcelFile(file);
            totalProcessed = excelProducts.size();
            
            logger.info("Parsed {} products from Excel file", totalProcessed);
            
            if (excelProducts.isEmpty()) {
                errors.add("No se encontraron productos válidos en el archivo Excel");
                return new ExcelImportResponse(0, 0, 0, errors, warnings);
            }
            
            // PHASE 1: VALIDATE ALL PRODUCTS FIRST (ALL-OR-NOTHING)
            logger.info("Phase 1: Validating all products before any import");
            for (int i = 0; i < excelProducts.size(); i++) {
                ExcelProductImportRequest excelProduct = excelProducts.get(i);
                int rowNumber = i + 2; // Excel row number (1-based + header)
                
                // Validate product data
                Set<ConstraintViolation<ExcelProductImportRequest>> violations = validator.validate(excelProduct);
                if (!violations.isEmpty()) {
                    StringBuilder errorMsg = new StringBuilder();
                    errorMsg.append("Fila ").append(rowNumber).append(": ");
                    for (ConstraintViolation<ExcelProductImportRequest> violation : violations) {
                        errorMsg.append(violation.getMessage()).append("; ");
                    }
                    errors.add(errorMsg.toString());
                }
                
                // Check if product ID already exists
                String productId = excelProduct.getProductId();
                if (productRepository.existsByIdAndStoreId(ProductId.of(productId), storeId)) {
                    errors.add("Fila " + rowNumber + ": El producto con ID '" + productId + 
                              "' ya existe en la tienda");
                }
                
                // Validate category existence/creation requirements
                validateCategoryRequirements(storeId, excelProduct, rowNumber, errors);
            }
            
            // If ANY errors found during validation, return immediately without importing ANYTHING
            if (!errors.isEmpty()) {
                logger.warn("Validation failed for Excel import. Found {} errors. No products will be imported.", errors.size());
                return new ExcelImportResponse(totalProcessed, 0, 0, errors, warnings);
            }
            
            // PHASE 2: ALL PRODUCTS ARE VALID - PROCEED WITH IMPORT (NO VALIDATION, ONLY CREATION)
            logger.info("Phase 2: All products validated successfully. Proceeding with import.");
            for (int i = 0; i < excelProducts.size(); i++) {
                ExcelProductImportRequest excelProduct = excelProducts.get(i);
                int rowNumber = i + 2;
                
                try {
                    // Handle category auto-creation (no validation, just creation)
                    boolean categoryCreated = handleCategoryCreationForValidProduct(
                        storeId, excelProduct, rowNumber, warnings
                    );
                    if (categoryCreated) {
                        categoriesCreated++;
                    }
                    
                    // Create product (should always succeed since we pre-validated)
                    createValidatedProductFromExcelData(storeId, excelProduct, rowNumber);
                    successfulImports++;
                    logger.debug("Successfully imported product: {} (row {})", 
                               excelProduct.getNombre(), rowNumber);
                    
                } catch (Exception e) {
                    logger.error("Unexpected error processing row {}: {}", rowNumber, e.getMessage(), e);
                    // If we get unexpected errors during import phase, we should rollback everything
                    throw new RuntimeException("Unexpected error during import phase for row " + rowNumber + ": " + e.getMessage(), e);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error during Excel import: {}", e.getMessage(), e);
            errors.add("Error procesando archivo Excel: " + e.getMessage());
            // This will cause transaction rollback
            throw new RuntimeException("Excel import failed", e);
        }
        
        logger.info("Excel import completed successfully. Processed: {}, Success: {}, Categories created: {}", 
                   totalProcessed, successfulImports, categoriesCreated);
        
        return new ExcelImportResponse(totalProcessed, successfulImports, categoriesCreated, errors, warnings);
    }
    
    /**
     * Validates category requirements during the pre-validation phase
     */
    private void validateCategoryRequirements(String storeId, ExcelProductImportRequest excelProduct, 
                                            int rowNumber, List<String> errors) {
        Integer categoryNumber = excelProduct.getCategoriaNumero();
        String categoryName = excelProduct.getCategoriaNombre();
        
        // Check if category exists
        Optional<com.sicua.application.category.dto.CategoryResponse> existingCategory = 
            categoryService.getCategoryByNumber(storeId, categoryNumber);
        
        if (!existingCategory.isPresent()) {
            // Category doesn't exist - validate if we can create it
            if (categoryName == null || categoryName.trim().isEmpty()) {
                errors.add("Fila " + rowNumber + ": Categoría " + categoryNumber + 
                          " no existe y no se proporcionó nombre para crearla");
            }
        }
        // If category exists, no validation needed - we'll use the existing one
    }
    
    /**
     * Handles category creation for already validated products (Phase 2)
     * No validation needed since Phase 1 already confirmed everything is valid
     */
    private boolean handleCategoryCreationForValidProduct(String storeId, ExcelProductImportRequest excelProduct, 
                                                         int rowNumber, List<String> warnings) {
        Integer categoryNumber = excelProduct.getCategoriaNumero();
        String categoryName = excelProduct.getCategoriaNombre();
        
        // Check if category already exists
        Optional<com.sicua.application.category.dto.CategoryResponse> existingCategory = 
            categoryService.getCategoryByNumber(storeId, categoryNumber);
        
        if (existingCategory.isPresent()) {
            // Category exists - use it (ignore any provided name)
            if (categoryName != null && !categoryName.trim().isEmpty() && 
                !categoryName.trim().equalsIgnoreCase(existingCategory.get().getName())) {
                warnings.add("Fila " + rowNumber + ": Categoría " + categoryNumber + 
                           " ya existe con nombre '" + existingCategory.get().getName() + 
                           "'. Se ignora el nombre proporcionado '" + categoryName + "'");
            }
            return false; // No new category created
        } else {
            // Category doesn't exist - create it (we already validated in Phase 1 that name is provided)
            CreateCategoryRequest createRequest = new CreateCategoryRequest();
            createRequest.setName(categoryName.trim());
            createRequest.setCategoryNumber(categoryNumber);
            
            categoryService.createCategory(storeId, createRequest);
            
            logger.info("Auto-created category {} with name '{}' from Excel import row {}", 
                       categoryNumber, categoryName, rowNumber);
            warnings.add("Fila " + rowNumber + ": Se creó automáticamente la categoría " + 
                       categoryNumber + " - '" + categoryName + "'");
            return true; // New category created
        }
    }
    
    /**
     * Creates a product from already validated Excel data (Phase 2)
     * No validation needed since Phase 1 already confirmed everything is valid
     */
    private void createValidatedProductFromExcelData(String storeId, ExcelProductImportRequest excelProduct,
                                                   int rowNumber) {
        // Use the client-provided product ID (already validated as unique in Phase 1)
        String productId = excelProduct.getProductId();
        
        // Create size field from talla and color
        String size = buildSizeField(excelProduct.getTalla(), excelProduct.getColor());
        
        // Create Product entity
        Product product = new Product(
            ProductId.of(productId),
            storeId,
            excelProduct.getNombre(),
            excelProduct.getMarca(), // Can be null
            excelProduct.getCategoriaNumero(),
            size,
            excelProduct.getPrecio(),
            excelProduct.getStock()
        );
        
        productRepository.save(product);
        logger.debug("Created product with ID {} from Excel row {}", productId, rowNumber);
    }
    
    private String buildSizeField(String talla, String color) {
        List<String> parts = new ArrayList<>();
        
        if (talla != null && !talla.trim().isEmpty()) {
            parts.add(talla.trim());
        }
        
        if (color != null && !color.trim().isEmpty()) {
            parts.add(color.trim());
        }
        
        return parts.isEmpty() ? null : String.join(" - ", parts);
    }
}
