package com.sicua.application.sale.usecase;

import com.sicua.application.auth.SessionService;
import com.sicua.application.excel.service.ExcelProcessingService;
import com.sicua.application.sale.dto.SaleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for exporting filtered sales to Excel format
 */
@Service
public class ExportSalesToExcelUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ExportSalesToExcelUseCase.class);
    
    private final ExcelProcessingService excelProcessingService;
    private final GetAllSalesUseCase getAllSalesUseCase;
    private final SessionService sessionService;
    
    public ExportSalesToExcelUseCase(ExcelProcessingService excelProcessingService,
                                   GetAllSalesUseCase getAllSalesUseCase,
                                   SessionService sessionService) {
        this.excelProcessingService = excelProcessingService;
        this.getAllSalesUseCase = getAllSalesUseCase;
        this.sessionService = sessionService;
    }
    
    @Transactional(readOnly = true)
    public byte[] execute(String dateFilterType, String startDate, String endDate, 
                         String selectedMonth, String statusFilter) {
        logger.info("Starting export of filtered sales to Excel. DateType: {}, Status: {}", 
                   dateFilterType, statusFilter);
        
        try {
            // Get all sales
            List<SaleResponse> allSales = getAllSalesUseCase.execute();
            logger.info("Retrieved {} total sales", allSales.size());
            
            // Apply filters
            List<SaleResponse> filteredSales = applyFilters(allSales, dateFilterType, 
                                                           startDate, endDate, selectedMonth, statusFilter);
            logger.info("After filtering: {} sales", filteredSales.size());
            
            if (filteredSales.isEmpty()) {
                logger.warn("No sales found after applying filters");
                // Return empty Excel with headers only
                return excelProcessingService.exportSalesToExcel(filteredSales);
            }
            
            // Export to Excel
            return excelProcessingService.exportSalesToExcel(filteredSales);
            
        } catch (Exception e) {
            logger.error("Error exporting sales to Excel: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to export sales to Excel: " + e.getMessage(), e);
        }
    }
    
    private List<SaleResponse> applyFilters(List<SaleResponse> sales, String dateFilterType,
                                          String startDate, String endDate, String selectedMonth, 
                                          String statusFilter) {
        return sales.stream()
                .filter(sale -> applyStatusFilter(sale, statusFilter))
                .filter(sale -> applyDateFilter(sale, dateFilterType, startDate, endDate, selectedMonth))
                .collect(Collectors.toList());
    }
    
    private boolean applyStatusFilter(SaleResponse sale, String statusFilter) {
        switch (statusFilter) {
            case "porFacturar":
                return !sale.getInvoiced();
            case "facturadas":
                return sale.getInvoiced();
            case "todas":
            default:
                return true;
        }
    }
    
    private boolean applyDateFilter(SaleResponse sale, String dateFilterType,
                                  String startDate, String endDate, String selectedMonth) {
        if ("all".equals(dateFilterType)) {
            return true;
        }
        
        try {
            // Parse sale date - handle both UTC and local timezone scenarios
            LocalDateTime saleDate = sale.getDate(); // SaleResponse.getDate() returns LocalDateTime directly
            
            if ("dateRange".equals(dateFilterType)) {
                if (startDate != null) {
                    LocalDate start = LocalDate.parse(startDate);
                    if (saleDate.toLocalDate().isBefore(start)) {
                        return false;
                    }
                }
                if (endDate != null) {
                    LocalDate end = LocalDate.parse(endDate);
                    if (saleDate.toLocalDate().isAfter(end)) {
                        return false;
                    }
                }
                return true;
            } else if ("month".equals(dateFilterType) && selectedMonth != null) {
                String[] yearMonth = selectedMonth.split("-");
                int year = Integer.parseInt(yearMonth[0]);
                int month = Integer.parseInt(yearMonth[1]);
                
                return saleDate.getYear() == year && saleDate.getMonthValue() == month;
            }
            
        } catch (Exception e) {
            logger.error("Error parsing date for filtering: {}, Sale date: {}", e.getMessage(), sale.getDate());
            return false;
        }
        
        return true;
    }
}
