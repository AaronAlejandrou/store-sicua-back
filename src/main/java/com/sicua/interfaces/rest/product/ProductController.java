package com.sicua.interfaces.rest.product;

import com.sicua.application.product.dto.CreateProductRequest;
import com.sicua.application.product.dto.ProductResponse;
import com.sicua.application.product.dto.UpdateProductRequest;
import com.sicua.application.product.usecase.CreateProductUseCase;
import com.sicua.application.product.usecase.DeleteProductUseCase;
import com.sicua.application.product.usecase.GetAllProductsUseCase;
import com.sicua.application.product.usecase.UpdateProductUseCase;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Product operations
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "API para gestión de productos del inventario")
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    
    public ProductController(CreateProductUseCase createProductUseCase,
                           GetAllProductsUseCase getAllProductsUseCase,
                           UpdateProductUseCase updateProductUseCase,
                           DeleteProductUseCase deleteProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }
    
    /**
     * Get all products
     */
    @GetMapping
    @Operation(
            summary = "Obtener todos los productos",
            description = "Retorna una lista con todos los productos del inventario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de productos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        logger.info("GET /api/products - Get all products");
        
        List<ProductResponse> products = getAllProductsUseCase.execute();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Create a new product
     */
    @PostMapping
    @Operation(
            summary = "Crear un nuevo producto",
            description = "Crea un nuevo producto en el inventario con la información proporcionada"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado exitosamente",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
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
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "Datos del producto a crear", required = true)
            @Valid @RequestBody CreateProductRequest request) {
        logger.info("POST /api/products - Create product: {}", request.getName());
        
        ProductResponse response = createProductUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Update an existing product
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un producto existente",
            description = "Actualiza la información de un producto existente en el inventario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id,
            @Parameter(description = "Nuevos datos del producto", required = true)
            @Valid @RequestBody UpdateProductRequest request) {
        logger.info("PUT /api/products/{} - Update product", id);
        
        ProductResponse response = updateProductUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete a product
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina un producto del inventario. Solo se puede eliminar si no tiene stock."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No se puede eliminar el producto (tiene stock)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id,
            @Parameter(description = "Force deletion even with stock", required = false)
            @RequestParam(value = "force", required = false, defaultValue = "false") boolean force) {
        logger.info("DELETE /api/products/{} - Delete product (force: {})", id, force);
        
        deleteProductUseCase.execute(id, force);
        return ResponseEntity.noContent().build();
    }
}
