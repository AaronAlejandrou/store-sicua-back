package com.sicua.application.sale.usecase;

import com.sicua.application.auth.SessionService;
import com.sicua.application.sale.dto.SaleResponse;
import com.sicua.domain.sale.entity.Sale;
import com.sicua.domain.sale.repository.SaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for retrieving all sales
 */
@Service
public class GetAllSalesUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(GetAllSalesUseCase.class);
    
    private final SaleRepository saleRepository;
    private final SessionService sessionService;
    
    public GetAllSalesUseCase(SaleRepository saleRepository, SessionService sessionService) {
        this.saleRepository = saleRepository;
        this.sessionService = sessionService;
    }
    
    @Transactional(readOnly = true)
    public List<SaleResponse> execute() {
        logger.info("Retrieving all sales");
        
        try {
            String storeId = sessionService.getCurrentStoreId();
            List<Sale> sales = saleRepository.findAllByStoreIdOrderByCreatedAtDesc(storeId);
            
            logger.info("Retrieved {} sales for store {}", sales.size(), storeId);
            
            return sales.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error retrieving sales: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve sales: " + e.getMessage(), e);
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
