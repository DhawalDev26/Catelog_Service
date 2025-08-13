package com.shopsphere.catalog.dto;// package com.shopespher.inventory.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockRequest {
    private Long tenantId;
    private String productId;
    private Integer quantity;
}
