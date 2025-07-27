package com.sicua.interfaces.rest.storeconfig;

import com.sicua.application.storeconfig.dto.StoreConfigResponse;
import com.sicua.application.storeconfig.dto.UpdateStoreConfigRequest;
import com.sicua.application.storeconfig.usecase.GetStoreConfigUseCase;
import com.sicua.application.storeconfig.usecase.UpdateStoreConfigUseCase;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Store Configuration operations
 */
@RestController
@RequestMapping("/api/store-config")
@Tag(name = "Store Configuration", description = "API para configuración de la tienda")
public class StoreConfigController {
    
    private static final Logger logger = LoggerFactory.getLogger(StoreConfigController.class);
    
    private final GetStoreConfigUseCase getStoreConfigUseCase;
    private final UpdateStoreConfigUseCase updateStoreConfigUseCase;
    
    public StoreConfigController(GetStoreConfigUseCase getStoreConfigUseCase,
                               UpdateStoreConfigUseCase updateStoreConfigUseCase) {
        this.getStoreConfigUseCase = getStoreConfigUseCase;
        this.updateStoreConfigUseCase = updateStoreConfigUseCase;
    }
    
    /**
     * Get store configuration
     */
    @GetMapping
    @Operation(
            summary = "Obtener configuración de la tienda",
            description = "Retorna la configuración actual de la tienda. Si no existe, crea una configuración por defecto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Configuración obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = StoreConfigResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<StoreConfigResponse> getStoreConfig() {
        logger.info("GET /api/store-config - Get store configuration");
        
        StoreConfigResponse response = getStoreConfigUseCase.execute();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update store configuration
     */
    @PutMapping
    @Operation(
            summary = "Actualizar configuración de la tienda",
            description = "Actualiza la configuración de la tienda con la información proporcionada"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Configuración actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = StoreConfigResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<StoreConfigResponse> updateStoreConfig(
            @Parameter(description = "Nueva configuración de la tienda", required = true)
            @Valid @RequestBody UpdateStoreConfigRequest request) {
        logger.info("PUT /api/store-config - Update store configuration");
        
        StoreConfigResponse response = updateStoreConfigUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
