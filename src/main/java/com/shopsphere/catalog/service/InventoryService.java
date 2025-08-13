package com.shopsphere.catalog.service;

import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.InventoryDTO;
import com.shopsphere.catalog.dto.StockRequest;
import com.shopsphere.catalog.dto.StockReservationRequest;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryService {

    APIResponse<InventoryDTO> addStock(StockRequest request);

    APIResponse<InventoryDTO> removeStock(StockRequest request);

    APIResponse<InventoryDTO> reserveStock(StockReservationRequest request);

    APIResponse<InventoryDTO> releaseStock(StockReservationRequest request);

    APIResponse<InventoryDTO> getStock(Long tenantId, String productId, Long warehouseId);
}
