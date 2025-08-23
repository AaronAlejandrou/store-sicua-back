package com.sicua.interfaces.rest.excel;

import com.sicua.application.excel.dto.ExcelImportResponse;
import com.sicua.application.excel.usecase.ExportProductsToExcelUseCase;
import com.sicua.application.excel.usecase.GenerateExcelTemplateUseCase;
import com.sicua.application.excel.usecase.ImportProductsFromExcelUseCase;
import com.sicua.interfaces.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * REST controller for Excel import/export operations
 */
@RestController
@RequestMapping("/api/products/excel")
@Tag(name = "Excel Import/Export", description = "API para importar y exportar productos mediante archivos Excel")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ExcelController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);
    
    private final ImportProductsFromExcelUseCase importProductsUseCase;
    private final ExportProductsToExcelUseCase exportProductsUseCase;
    private final GenerateExcelTemplateUseCase generateTemplateUseCase;
    
    public ExcelController(ImportProductsFromExcelUseCase importProductsUseCase,
                         ExportProductsToExcelUseCase exportProductsUseCase,
                         GenerateExcelTemplateUseCase generateTemplateUseCase) {
        this.importProductsUseCase = importProductsUseCase;
        this.exportProductsUseCase = exportProductsUseCase;
        this.generateTemplateUseCase = generateTemplateUseCase;
    }
    
    /**
     * Import products from Excel file
     */
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Importar productos desde Excel",
            description = "Importa productos masivamente desde un archivo Excel. Crea automáticamente categorías nuevas si se proporciona el nombre."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Importación completada (puede incluir errores parciales)",
                    content = @Content(schema = @Schema(implementation = ExcelImportResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Archivo Excel inválido o formato incorrecto",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "413",
                    description = "Archivo demasiado grande",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ExcelImportResponse> importProducts(
            @Parameter(description = "Archivo Excel con los productos a importar (.xlsx)", required = true)
            @RequestParam("file") MultipartFile file) {
        
        logger.info("POST /api/products/excel/import - Import products from Excel file: {}", file.getOriginalFilename());
        
        // Validate file
        if (file.isEmpty()) {
            logger.warn("Empty file uploaded");
            return ResponseEntity.badRequest().body(
                new ExcelImportResponse(0, 0, 0, 
                    java.util.List.of("El archivo está vacío"), 
                    java.util.List.of())
            );
        }
        
        if (!isExcelFile(file)) {
            logger.warn("Non-Excel file uploaded: {}", file.getOriginalFilename());
            return ResponseEntity.badRequest().body(
                new ExcelImportResponse(0, 0, 0, 
                    java.util.List.of("El archivo debe ser un Excel (.xlsx)"), 
                    java.util.List.of())
            );
        }
        
        try {
            ExcelImportResponse response = importProductsUseCase.execute(file);
            
            // Return success status even if there were partial errors
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error importing products from Excel: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExcelImportResponse(0, 0, 0, 
                    java.util.List.of("Error procesando archivo: " + e.getMessage()), 
                    java.util.List.of())
            );
        }
    }
    
    /**
     * Download Excel template
     */
    @GetMapping("/template")
    @Operation(
            summary = "Descargar plantilla Excel",
            description = "Genera y descarga una plantilla Excel con el formato correcto para importar productos, incluyendo ejemplos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Plantilla Excel generada exitosamente",
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<byte[]> downloadTemplate() {
        logger.info("GET /api/products/excel/template - Generate Excel template");
        
        try {
            byte[] templateBytes = generateTemplateUseCase.execute();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "plantilla_productos.xlsx");
            headers.setContentLength(templateBytes.length);
            
            logger.info("Excel template generated and returned successfully");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(templateBytes);
                    
        } catch (Exception e) {
            logger.error("Error generating Excel template: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Export all products to Excel
     */
    @GetMapping("/export")
    @Operation(
            summary = "Exportar inventario a Excel",
            description = "Exporta todos los productos del inventario actual a un archivo Excel."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario exportado exitosamente",
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<byte[]> exportProducts() {
        logger.info("GET /api/products/excel/export - Export all products to Excel");
        
        try {
            byte[] excelBytes = exportProductsUseCase.execute();
            
            // Generate filename with timestamp (using local time)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "inventario_" + timestamp + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(excelBytes.length);
            
            logger.info("Products exported to Excel successfully: {}", filename);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
                    
        } catch (Exception e) {
            logger.error("Error exporting products to Excel: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private boolean isExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        
        return (contentType != null && contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) ||
               (originalFilename != null && originalFilename.toLowerCase().endsWith(".xlsx"));
    }
}
