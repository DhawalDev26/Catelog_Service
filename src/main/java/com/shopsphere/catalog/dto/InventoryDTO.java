package com.shopsphere.catalog.dto;// package com.shopespher.inventory.dto;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDTO {
    private Long id;
    private Long tenantId;
    private Long productId;
    private Long warehouseId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer safetyStock;
    private Instant lastUpdated;
}
