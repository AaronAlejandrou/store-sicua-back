package com.sicua.application.sale.usecase;

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
    
    public GetAllSalesUseCase(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }
    
    @Transactional(readOnly = true)
    public List<SaleResponse> execute() {
        logger.info("Retrieving all sales");
        
        try {
            List<Sale> sales = saleRepository.findAllOrderByCreatedAtDesc();
            
            logger.info("Retrieved {} sales", sales.size());
            
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
