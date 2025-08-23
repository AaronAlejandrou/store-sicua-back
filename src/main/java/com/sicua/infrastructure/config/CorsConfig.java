package com.sicua.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * CORS Configuration for SICUA Backend
 * 
 * Cross-Origin Resource Sharing (CORS) configuration that enables
 * secure communication between the React frontend and Spring Boot backend.
 * 
 * Configuration Strategy:
 * - Environment-based origin configuration via application.properties
 * - Supports both specific origins and wildcard patterns
 * - Dual configuration: WebMvcConfigurer + CorsConfigurationSource
 * - Session-based authentication with credentials support
 * 
 * Environment Configuration:
 * - sicua.cors.allowed-origins: Comma-separated list of allowed origins
 * - Default: http://localhost:5173 (Vite development server)
 * - Production: Set via environment variable or application.properties
 * 
 * Development Setup:
 * - Frontend: localhost:5173 (Vite dev server)
 * - Backend: localhost:8080 (Spring Boot)
 * - Proxy: Vite forwards /api/* to localhost:8080
 * 
 * Production Setup:
 * - Frontend: Deployed URL (Railway/Vercel/etc.)
 * - Backend: Railway production URL
 * - Direct cross-origin requests with CORS headers
 * 
 * @see SecurityConfig.java for session management
 * @see application.properties for CORS origin configuration
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Allowed Origins Configuration
     * 
     * Injected from application.properties or environment variables.
     * Can be a single origin or comma-separated list of origins.
     * Use "*" for development only (less secure).
     * 
     * Examples:
     * - Single: http://localhost:5173
     * - Multiple: http://localhost:5173,https://sicua-frontend.vercel.app
     * - Wildcard: * (development only)
     */
    @Value("${sicua.cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;

    /**
     * WebMVC CORS Configuration
     * 
     * Configures CORS at the Spring MVC level for standard HTTP requests.
     * This handles most API requests and provides the first layer of CORS support.
     * 
     * Key Features:
     * - Maps all /api/** endpoints
     * - Supports both specific origins and wildcard patterns
     * - Enables credentials for session-based authentication
     * - Allows all standard HTTP methods and headers
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Check if wildcard is configured (development mode)
        if ("*".equals(allowedOrigins)) {
            registry.addMapping("/api/**")
                    .allowedOriginPatterns("*")  // Allow all origins (development only)
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")         // Allow all headers
                    .allowCredentials(true);     // Enable session cookies
        } else {
            // Parse comma-separated origins for production
            List<String> origins = Arrays.asList(allowedOrigins.split(","));
            registry.addMapping("/api/**")
                    .allowedOriginPatterns(origins.toArray(new String[0]))
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }

    /**
     * CORS Configuration Source Bean
     * 
     * Provides CORS configuration for Spring Security and other filters.
     * This is the second layer of CORS support and works with Spring Security.
     * 
     * Why Both Configurations?
     * 1. WebMvcConfigurer: Handles standard MVC requests
     * 2. CorsConfigurationSource: Integrates with Spring Security filters
     * 3. Together: Provides comprehensive CORS support for all scenarios
     * 
     * @return Configured CORS source for security and filter chains
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configure allowed origins based on environment setting
        if ("*".equals(allowedOrigins)) {
            // Development: Allow all origins with pattern matching
            configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        } else {
            // Production: Parse and set specific origins
            List<String> origins = Arrays.asList(allowedOrigins.split(","));
            configuration.setAllowedOriginPatterns(origins);
        }
        
        /**
         * HTTP Methods Configuration
         * Covers all REST API operations plus OPTIONS for preflight
         */
        configuration.setAllowedMethods(Arrays.asList(
            "GET",     // Read operations
            "POST",    // Create operations  
            "PUT",     // Update operations
            "DELETE",  // Delete operations
            "OPTIONS"  // Preflight requests
        ));
        
        /**
         * Headers Configuration
         * Allows all headers for maximum compatibility
         * 
         * Note: In production, consider restricting to specific headers:
         * - Content-Type, Authorization, Accept, Origin, X-Requested-With
         */
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        /**
         * Credentials Support
         * CRITICAL: Enables session cookies across origins
         * Required for session-based authentication
         */
        configuration.setAllowCredentials(true);

        /**
         * URL Pattern Registration
         * Applies CORS to all API endpoints (/api/**)
         */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
