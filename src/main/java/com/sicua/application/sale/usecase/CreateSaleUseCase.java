package com.sicua.application.sale.usecase;

import com.sicua.application.sale.dto.CreateSaleRequest;
import com.sicua.application.sale.dto.SaleItemRequest;
import com.sicua.application.sale.dto.SaleResponse;
import com.sicua.domain.product.valueobject.ProductId;
import com.sicua.domain.sale.entity.Sale;
import com.sicua.domain.sale.entity.SaleItem;
import com.sicua.domain.sale.repository.SaleRepository;
import com.sicua.domain.sale.service.SaleDomainService;
import com.sicua.domain.sale.valueobject.SaleId;
import com.sicua.application.auth.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for creating a new sale
 */
@Service
public class CreateSaleUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CreateSaleUseCase.class);
    
    private final SaleRepository saleRepository;
    private final SaleDomainService saleDomainService;
    private final SessionService sessionService;
    
    public CreateSaleUseCase(SaleRepository saleRepository, SaleDomainService saleDomainService, SessionService sessionService) {
        this.saleRepository = saleRepository;
        this.saleDomainService = saleDomainService;
        this.sessionService = sessionService;
    }
    
    @Transactional
    public SaleResponse execute(CreateSaleRequest request) {
        logger.info("Creating new sale for client: {}", request.getClientName());
        
        try {
            List<SaleItem> saleItems = request.getItems().stream()
                    .map(this::mapToSaleItem)
                    .collect(Collectors.toList());
            
            Sale sale = new Sale(
                    SaleId.generate(),
                    sessionService.getCurrentStoreId(),
                    request.getClientDni(),
                    request.getClientName(),
                    saleItems
            );
            
            // Validate stock availability
            if (!saleDomainService.validateStockAvailability(sale)) {
                throw new IllegalStateException("Insufficient stock for one or more items");
            }
            
            // Reduce stock for all products in the sale
            saleDomainService.reduceStockForSale(sale);
            
            Sale savedSale = saleRepository.save(sale);
            
            logger.info("Sale created successfully with ID: {}", savedSale.getId().getValue());
            
            return mapToResponse(savedSale);
            
        } catch (IllegalStateException e) {
            logger.warn("Sale creation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error creating sale: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create sale: " + e.getMessage(), e);
        }
    }
    
    private SaleItem mapToSaleItem(SaleItemRequest request) {
        return new SaleItem(
                ProductId.of(request.getProductId()),
                request.getName(),
                request.getPrice(),
                request.getQuantity()
        );
    }
    
    private SaleResponse mapToResponse(Sale sale) {
        List<SaleResponse.SaleItemResponse> itemResponses = sale.getItems().stream()
                .map(item -> new SaleResponse.SaleItemResponse(
                        item.getProductId().getValue(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());
        
        return new SaleResponse(
                sale.getId().getValue(),
                sale.getClientDni(),
                sale.getClientName(),
                sale.getDate(),
                itemResponses,
                sale.getTotal(),
                sale.getInvoiced(),
                sale.getCreatedAt()
        );
    }
}
