package com.sicua.interfaces.rest.auth;

import com.sicua.domain.storeconfig.entity.StoreConfig;
import com.sicua.domain.storeconfig.repository.StoreConfigRepository;
import com.sicua.interfaces.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Simple authentication controller using sessions
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API para autenticación simple de tiendas")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String SESSION_STORE_ID = "storeId";
    
    private final StoreConfigRepository storeConfigRepository;
    
    public AuthController(StoreConfigRepository storeConfigRepository) {
        this.storeConfigRepository = storeConfigRepository;
    }
    
    @PostMapping("/register")
    @Operation(summary = "Registrar nueva tienda", description = "Registra una nueva tienda con su información y contraseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tienda registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya existe", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpSession session) {
        logger.info("POST /api/auth/register - Register store with email: {}", request.getEmail());
        
        // Check if email already exists
        if (storeConfigRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // Create new store config with password
        StoreConfig storeConfig = new StoreConfig(
            request.getStoreName(),
            request.getStoreAddress(),
            request.getEmail(),
            request.getStorePhone(),
            request.getPassword()
        );
        
        storeConfig = storeConfigRepository.save(storeConfig);
        
        // Store in session
        session.setAttribute(SESSION_STORE_ID, storeConfig.getId());
        
        logger.info("Store registered successfully with ID: {}", storeConfig.getId());
        
        return ResponseEntity.ok(new AuthResponse(
            storeConfig.getId(),
            storeConfig.getName(),
            storeConfig.getEmail(),
            "Tienda registrada exitosamente"
        ));
    }
    
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Inicia sesión con email y contraseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión iniciada exitosamente"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        logger.info("POST /api/auth/login - Login attempt for email: {}", request.getEmail());
        logger.info("Session ID before login: {}", session.getId());
        logger.info("Session is new: {}", session.isNew());
        
        StoreConfig storeConfig = storeConfigRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
        
        if (!storeConfig.validatePassword(request.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        
        // Store in session
        session.setAttribute(SESSION_STORE_ID, storeConfig.getId());
        
        logger.info("Login successful for store: {}", storeConfig.getName());
        logger.info("Session ID after login: {}", session.getId());
        logger.info("Store ID stored in session: {}", session.getAttribute(SESSION_STORE_ID));
        
        return ResponseEntity.ok(new AuthResponse(
            storeConfig.getId(),
            storeConfig.getName(),
            storeConfig.getEmail(),
            "Sesión iniciada exitosamente"
        ));
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Cierra la sesión actual")
    public ResponseEntity<String> logout(HttpSession session) {
        logger.info("POST /api/auth/logout - Logout");
        session.invalidate();
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }
    
    @GetMapping("/status")
    @Operation(summary = "Verificar estado de sesión", description = "Verifica si hay una sesión activa")
    public ResponseEntity<AuthResponse> getStatus(HttpSession session) {
        String storeId = (String) session.getAttribute(SESSION_STORE_ID);
        
        if (storeId == null) {
            return ResponseEntity.status(401).build();
        }
        
        StoreConfig storeConfig = storeConfigRepository.findById(storeId)
            .orElse(null);
        
        if (storeConfig == null) {
            session.invalidate();
            return ResponseEntity.status(401).build();
        }
        
        return ResponseEntity.ok(new AuthResponse(
            storeConfig.getId(),
            storeConfig.getName(),
            storeConfig.getEmail(),
            "Sesión activa"
        ));
    }
}
