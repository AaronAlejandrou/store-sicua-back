package com.sicua.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database Configuration for SICUA Backend
 * 
 * This configuration class sets up:
 * - JPA Repository scanning for persistence layer
 * - Transaction management for data consistency
 * - PostgreSQL database integration with Supabase
 * 
 * Database Connection Details:
 * - Provider: Supabase (PostgreSQL)
 * - Connection Pool: HikariCP (configured in application.properties)
 * - Repository Package: com.sicua.infrastructure.persistence
 * 
 * @see com.sicua.infrastructure.persistence for repository implementations
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.sicua.infrastructure.persistence")
@EnableTransactionManagement
public class DatabaseConfig {
    
    // Configuration is handled via application.properties
    // This class enables JPA features and transaction management
}
