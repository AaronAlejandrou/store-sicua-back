package com.sicua.application.excel.usecase;

import com.sicua.application.excel.service.ExcelProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Use case for generating Excel template for product imports
 */
@Service
public class GenerateExcelTemplateUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(GenerateExcelTemplateUseCase.class);
    
    private final ExcelProcessingService excelProcessingService;
    
    public GenerateExcelTemplateUseCase(ExcelProcessingService excelProcessingService) {
        this.excelProcessingService = excelProcessingService;
    }
    
    public byte[] execute() {
        logger.info("Generating Excel template for product import");
        
        try {
            byte[] templateBytes = excelProcessingService.generateTemplate();
            logger.info("Excel template generated successfully");
            return templateBytes;
            
        } catch (Exception e) {
            logger.error("Error generating Excel template: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate Excel template: " + e.getMessage(), e);
        }
    }
}
