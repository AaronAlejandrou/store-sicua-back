package com.sicua.application.sale.usecase;

import com.sicua.application.auth.SessionService;
import com.sicua.application.sale.dto.SaleResponse;
import com.sicua.domain.sale.entity.Sale;
import com.sicua.domain.sale.repository.SaleRepository;
import com.sicua.domain.sale.service.SaleDomainService;
import com.sicua.domain.sale.valueobject.SaleId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for marking a sale as invoiced
 */
@Service
public class MarkSaleAsInvoicedUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(MarkSaleAsInvoicedUseCase.class);
    
    private final SaleRepository saleRepository;
    private final SaleDomainService saleDomainService;
    private final SessionService sessionService;
    
    public MarkSaleAsInvoicedUseCase(SaleRepository saleRepository, SaleDomainService saleDomainService, SessionService sessionService) {
        this.saleRepository = saleRepository;
        this.saleDomainService = saleDomainService;
        this.sessionService = sessionService;
    }
    
    @Transactional
    public SaleResponse execute(String saleId) {
        logger.info("Marking sale as invoiced: {}", saleId);
        
        try {
            SaleId id = SaleId.of(saleId);
            String storeId = sessionService.getCurrentStoreId();
            
            Sale sale = saleRepository.findByIdAndStoreId(id, storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
            
            if (!saleDomainService.canMarkAsInvoiced(sale)) {
                throw new IllegalStateException("Sale cannot be marked as invoiced");
            }
            
            sale.markAsInvoiced();
            Sale updatedSale = saleRepository.save(sale);
            
            logger.info("Sale marked as invoiced successfully: {}", saleId);
            
            return mapToResponse(updatedSale);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Cannot mark sale as invoiced: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error marking sale as invoiced: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to mark sale as invoiced: " + e.getMessage(), e);
        }
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
