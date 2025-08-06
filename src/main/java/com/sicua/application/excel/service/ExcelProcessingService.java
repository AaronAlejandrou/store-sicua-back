package com.sicua.application.excel.service;

import com.sicua.application.excel.dto.ExcelProductImportRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for Excel file processing - reading and writing
 */
@Service
public class ExcelProcessingService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelProcessingService.class);
    
    // Excel column indices (0-based)
    private static final int COL_PRODUCTO_ID = 0;
    private static final int COL_NOMBRE = 1;
    private static final int COL_PRECIO = 2;
    private static final int COL_TALLA = 3;
    private static final int COL_COLOR = 4;
    private static final int COL_CATEGORIA_NUMERO = 5;
    private static final int COL_CATEGORIA_NOMBRE = 6;
    private static final int COL_STOCK = 7;
    private static final int COL_MARCA = 8;
    
    // Excel headers
    private static final String[] HEADERS = {
        "Producto_ID", "Nombre", "Precio", "Talla", "Color", 
        "Categoria_Numero", "Categoria_Nombre", "Stock", "Marca"
    };
    
    /**
     * Parse Excel file and extract product data
     */
    public List<ExcelProductImportRequest> parseExcelFile(MultipartFile file) throws IOException {
        logger.info("Parsing Excel file: {}", file.getOriginalFilename());
        
        List<ExcelProductImportRequest> products = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row (row 0)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }
                
                try {
                    ExcelProductImportRequest product = parseRowToProduct(row, rowIndex);
                    if (product != null) {
                        products.add(product);
                    }
                } catch (Exception e) {
                    logger.warn("Error parsing row {}: {}", rowIndex + 1, e.getMessage());
                    // Continue processing other rows
                }
            }
        }
        
        logger.info("Parsed {} products from Excel file", products.size());
        return products;
    }
    
    /**
     * Generate Excel template with headers and sample data
     */
    public byte[] generateTemplate() throws IOException {
        logger.info("Generating Excel template");
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Productos");
            
            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Add sample data
            addSampleData(sheet, workbook);
            
            // Auto-size columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
            logger.info("Excel template generated successfully");
            return outputStream.toByteArray();
        }
    }
    
    /**
     * Export products to Excel format
     */
    public byte[] exportProductsToExcel(List<ExcelProductImportRequest> products) throws IOException {
        logger.info("Exporting {} products to Excel", products.size());
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Inventario");
            
            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Add product data
            for (int i = 0; i < products.size(); i++) {
                Row row = sheet.createRow(i + 1);
                ExcelProductImportRequest product = products.get(i);
                
                createCell(row, COL_PRODUCTO_ID, product.getProductId(), dataStyle);
                createCell(row, COL_NOMBRE, product.getNombre(), dataStyle);
                createCell(row, COL_PRECIO, product.getPrecio(), dataStyle);
                createCell(row, COL_TALLA, product.getTalla(), dataStyle);
                createCell(row, COL_COLOR, product.getColor(), dataStyle);
                createCell(row, COL_CATEGORIA_NUMERO, product.getCategoriaNumero(), dataStyle);
                createCell(row, COL_CATEGORIA_NOMBRE, product.getCategoriaNombre(), dataStyle);
                createCell(row, COL_STOCK, product.getStock(), dataStyle);
                createCell(row, COL_MARCA, product.getMarca(), dataStyle);
            }
            
            // Auto-size columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
            logger.info("Excel export completed successfully");
            return outputStream.toByteArray();
        }
    }
    
    private ExcelProductImportRequest parseRowToProduct(Row row, int rowIndex) {
        // Validate required fields first
        String productoId = getCellStringValue(row.getCell(COL_PRODUCTO_ID));
        if (productoId == null || productoId.trim().isEmpty()) {
            throw new IllegalArgumentException("Producto_ID is required in row " + (rowIndex + 1));
        }
        
        String nombre = getCellStringValue(row.getCell(COL_NOMBRE));
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre is required in row " + (rowIndex + 1));
        }
        
        BigDecimal precio = getCellBigDecimalValue(row.getCell(COL_PRECIO));
        if (precio == null) {
            throw new IllegalArgumentException("Precio is required in row " + (rowIndex + 1));
        }
        
        Integer categoriaNumero = getCellIntegerValue(row.getCell(COL_CATEGORIA_NUMERO));
        if (categoriaNumero == null) {
            throw new IllegalArgumentException("Categoria_Numero is required in row " + (rowIndex + 1));
        }
        
        Integer stock = getCellIntegerValue(row.getCell(COL_STOCK));
        if (stock == null) {
            throw new IllegalArgumentException("Stock is required in row " + (rowIndex + 1));
        }
        
        // Optional fields
        String talla = getCellStringValue(row.getCell(COL_TALLA));
        String color = getCellStringValue(row.getCell(COL_COLOR));
        String categoriaNombre = getCellStringValue(row.getCell(COL_CATEGORIA_NOMBRE));
        String marca = getCellStringValue(row.getCell(COL_MARCA));
        
        return new ExcelProductImportRequest(
            productoId.trim(), nombre.trim(), precio, talla, color,
            categoriaNumero, categoriaNombre, stock, marca
        );
    }
    
    private boolean isEmptyRow(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellStringValue(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numValue = cell.getNumericCellValue();
                    // Check if it's a whole number
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
    
    private BigDecimal getCellBigDecimalValue(Cell cell) {
        if (cell == null) return null;
        
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    if (value.isEmpty()) return null;
                    return new BigDecimal(value);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid number format in cell: {}", e.getMessage());
            return null;
        }
    }
    
    private Integer getCellIntegerValue(Cell cell) {
        if (cell == null) return null;
        
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) cell.getNumericCellValue();
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    if (value.isEmpty()) return null;
                    return Integer.valueOf(value);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer format in cell: {}", e.getMessage());
            return null;
        }
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private void createCell(Row row, int columnIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else {
            cell.setCellValue(value.toString());
        }
        
        if (style != null) {
            cell.setCellStyle(style);
        }
    }
    
    private void addSampleData(Sheet sheet, Workbook workbook) {
        // No style for sample data (no borders)
        
        // Sample row 1
        Row row1 = sheet.createRow(1);
        createCell(row1, COL_PRODUCTO_ID, "EXAMPLE-001", null);
        createCell(row1, COL_NOMBRE, "Nombre Ejemplo", null);
        createCell(row1, COL_PRECIO, new BigDecimal("99.99"), null);
        createCell(row1, COL_TALLA, "XL o 32 o G", null);
        createCell(row1, COL_COLOR, "Ejemplo Color", null);
        createCell(row1, COL_CATEGORIA_NUMERO, 9999, null);
        createCell(row1, COL_CATEGORIA_NOMBRE, "Ejemplo Categoria", null);
        createCell(row1, COL_STOCK, 9999, null);
        createCell(row1, COL_MARCA, "Ejemplo Marca", null);
    }
}
