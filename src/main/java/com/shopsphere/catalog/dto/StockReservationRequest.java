package com.shopsphere.catalog.dto;// package com.shopespher.inventory.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockReservationRequest {
    private Long tenantId;
    private String productId;
    private Long warehouseId;
    private Integer quantity; // quantity to reserve (positive)
    private String referenceId; // order id
}
