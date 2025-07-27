package com.sicua.application.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Datos para crear una nueva venta")
public class CreateSaleRequest {
    
    @Schema(description = "DNI del cliente", example = "12345678")
    private String clientDni;
    
    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String clientName;
    
    @Schema(description = "Lista de items de la venta")
    @NotEmpty(message = "Sale must have at least one item")
    @Valid
    private List<SaleItemRequest> items;

    public CreateSaleRequest() {}

    public CreateSaleRequest(String clientDni, String clientName, List<SaleItemRequest> items) {
        this.clientDni = clientDni;
        this.clientName = clientName;
        this.items = items;
    }

    // Getters and Setters
    public String getClientDni() {
        return clientDni;
    }

    public void setClientDni(String clientDni) {
        this.clientDni = clientDni;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<SaleItemRequest> getItems() {
        return items;
    }

    public void setItems(List<SaleItemRequest> items) {
        this.items = items;
    }
}
