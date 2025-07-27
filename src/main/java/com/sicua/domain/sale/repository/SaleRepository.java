package com.sicua.domain.sale.repository;

import com.sicua.domain.sale.entity.Sale;
import com.sicua.domain.sale.valueobject.SaleId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Sale domain entity
 */
public interface SaleRepository {
    
    /**
     * Find sale by its ID
     * @param saleId the sale identifier
     * @return Optional containing the sale if found
     */
    Optional<Sale> findById(SaleId saleId);
    
    /**
     * Find all sales
     * @return List of all sales
     */
    List<Sale> findAll();
    
    /**
     * Save a sale
     * @param sale the sale to save
     * @return the saved sale
     */
    Sale save(Sale sale);
    
    /**
     * Find all sales ordered by creation date descending
     * @return List of sales ordered by creation date
     */
    List<Sale> findAllOrderByCreatedAtDesc();
    
    /**
     * Find all invoiced sales
     * @return List of invoiced sales
     */
    List<Sale> findByInvoiced(boolean invoiced);
}
