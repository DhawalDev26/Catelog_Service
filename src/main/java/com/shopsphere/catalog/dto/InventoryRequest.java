package com.shopsphere.catalog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryRequest {
    private String warehouseAddress;
    private Integer quantity;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer safetyStock;
    private String referenceId;
    private String note;
}
