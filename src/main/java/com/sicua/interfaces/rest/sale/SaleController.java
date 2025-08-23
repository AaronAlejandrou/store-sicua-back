package com.sicua.interfaces.rest.sale;

import com.sicua.application.sale.dto.CreateSaleRequest;
import com.sicua.application.sale.dto.SaleResponse;
import com.sicua.application.sale.usecase.CreateSaleUseCase;
import com.sicua.application.sale.usecase.GetAllSalesUseCase;
import com.sicua.application.sale.usecase.MarkSaleAsInvoicedUseCase;
import com.sicua.application.sale.usecase.ExportSalesToExcelUseCase;
import com.sicua.interfaces.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * REST controller for Sale operations
 */
@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sales", description = "API para gestión de ventas y facturación")
public class SaleController {
    
    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);
    
    private final CreateSaleUseCase createSaleUseCase;
    private final GetAllSalesUseCase getAllSalesUseCase;
    private final MarkSaleAsInvoicedUseCase markSaleAsInvoicedUseCase;
    private final ExportSalesToExcelUseCase exportSalesToExcelUseCase;
    
    public SaleController(CreateSaleUseCase createSaleUseCase,
                         GetAllSalesUseCase getAllSalesUseCase,
                         MarkSaleAsInvoicedUseCase markSaleAsInvoicedUseCase,
                         ExportSalesToExcelUseCase exportSalesToExcelUseCase) {
        this.createSaleUseCase = createSaleUseCase;
        this.getAllSalesUseCase = getAllSalesUseCase;
        this.markSaleAsInvoicedUseCase = markSaleAsInvoicedUseCase;
        this.exportSalesToExcelUseCase = exportSalesToExcelUseCase;
    }
    
    /**
     * Get all sales
     */
    @GetMapping
    @Operation(
            summary = "Obtener todas las ventas",
            description = "Retorna una lista con todas las ventas ordenadas por fecha de creación (más recientes primero)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de ventas obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = SaleResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<SaleResponse>> getAllSales() {
        logger.info("GET /api/sales - Get all sales");
        
        List<SaleResponse> sales = getAllSalesUseCase.execute();
        return ResponseEntity.ok(sales);
    }
    
    /**
     * Create a new sale
     */
    @PostMapping
    @Operation(
            summary = "Crear una nueva venta",
            description = "Crea una nueva venta con múltiples items. Automáticamente reduce el stock de los productos vendidos y calcula el total."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Venta creada exitosamente",
                    content = @Content(schema = @Schema(implementation = SaleResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Stock insuficiente para uno o más productos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<SaleResponse> createSale(
            @Parameter(description = "Datos de la venta a crear", required = true)
            @Valid @RequestBody CreateSaleRequest request) {
        logger.info("POST /api/sales - Create sale for client: {}", request.getClientName());
        
        SaleResponse response = createSaleUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Mark sale as invoiced
     */
    @PutMapping("/{id}/invoice")
    @Operation(
            summary = "Marcar venta como facturada",
            description = "Cambia el estado de una venta a 'facturada'. Una vez facturada, no se puede revertir."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Venta marcada como facturada exitosamente",
                    content = @Content(schema = @Schema(implementation = SaleResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La venta ya está facturada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venta no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<SaleResponse> markSaleAsInvoiced(
            @Parameter(description = "ID de la venta a marcar como facturada", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        logger.info("PUT /api/sales/{}/invoice - Mark sale as invoiced", id);
        
        SaleResponse response = markSaleAsInvoicedUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Export filtered sales to Excel
     */
    @GetMapping("/excel/export")
    @Operation(
            summary = "Exportar ventas filtradas a Excel",
            description = "Exporta las ventas aplicando los filtros especificados (fecha, estado) a un archivo Excel."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ventas exportadas exitosamente",
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<byte[]> exportFilteredSales(
            @Parameter(description = "Tipo de filtro de fecha", example = "all")
            @RequestParam(value = "dateFilterType", defaultValue = "all") String dateFilterType,
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", example = "2024-01-01")
            @RequestParam(value = "startDate", required = false) String startDate,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)", example = "2024-12-31")
            @RequestParam(value = "endDate", required = false) String endDate,
            @Parameter(description = "Mes seleccionado (YYYY-MM)", example = "2024-01")
            @RequestParam(value = "selectedMonth", required = false) String selectedMonth,
            @Parameter(description = "Filtro de estado", example = "todas")
            @RequestParam(value = "statusFilter", defaultValue = "todas") String statusFilter) {
        
        logger.info("GET /api/sales/excel/export - Export filtered sales with filters: dateType={}, status={}", 
                   dateFilterType, statusFilter);
        
        try {
            byte[] excelBytes = exportSalesToExcelUseCase.execute(
                dateFilterType, startDate, endDate, selectedMonth, statusFilter);
            
            // Generate filename with timestamp (using local time) and filters
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            StringBuilder filename = new StringBuilder("ventas");
            
            if ("dateRange".equals(dateFilterType) && startDate != null && endDate != null) {
                filename.append("_").append(startDate).append("_al_").append(endDate);
            } else if ("month".equals(dateFilterType) && selectedMonth != null) {
                filename.append("_").append(selectedMonth);
            } else if ("all".equals(dateFilterType)) {
                filename.append("_todas");
            }
            
            if (!"todas".equals(statusFilter)) {
                filename.append("_").append(statusFilter);
            }
            
            filename.append("_").append(timestamp).append(".xlsx");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename.toString());
            headers.setContentLength(excelBytes.length);
            
            logger.info("Sales exported to Excel successfully: {}", filename);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
                    
        } catch (Exception e) {
            logger.error("Error exporting sales to Excel: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
