package com.sicua.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration for SICUA Backend API documentation
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server")
                ))
                .info(new Info()
                        .title("SICUA Backend API")
                        .description("Sistema de Inventario y Control de Ventas - Backend REST API\n\n" +
                                "Este backend implementa arquitectura DDD (Domain Driven Design) y proporciona " +
                                "APIs para la gestión de productos, ventas y configuración de tienda.\n\n" +
                                "**Características principales:**\n" +
                                "- Gestión completa de productos con control de stock\n" +
                                "- Sistema de ventas con múltiples items\n" +
                                "- Control automático de inventario\n" +
                                "- Configuración de tienda\n" +
                                "- Validaciones de negocio\n" +
                                "- Transacciones seguras")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SICUA Development Team")
                                .email("dev@sicua.com")
                                .url("https://sicua.com"))
                        .license(new License()
                                .name("Private License")
                                .url("https://sicua.com/license")));
    }
}
