package com.shopsphere.catalog.controller;

import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.InventoryDTO;
import com.shopsphere.catalog.dto.StockRequest;
import com.shopsphere.catalog.dto.StockReservationRequest;
import com.shopsphere.catalog.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/addStock")
    public APIResponse<InventoryDTO> addStock(@RequestBody StockRequest request) {
        try {
            return inventoryService.addStock(request);
        } catch (Exception ex) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null,
                    "Failed to add stock: " + ex.getMessage());
        }
    }

    @PostMapping("/removeStock")
    public APIResponse<InventoryDTO> removeStock(@RequestBody StockRequest request) {
        try {
            return inventoryService.removeStock(request);
        } catch (Exception ex) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null,
                    "Failed to remove stock: " + ex.getMessage());
        }
    }

    @PostMapping("/reserveStock")
    public APIResponse<InventoryDTO> reserveStock(@RequestBody StockReservationRequest request) {
        try {
            return inventoryService.reserveStock(request);
        } catch (Exception ex) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null,
                    "Failed to reserve stock: " + ex.getMessage());
        }
    }

    @PostMapping("/releaseStock")
    public APIResponse<InventoryDTO> releaseStock(@RequestBody StockReservationRequest request) {
        try {
            return inventoryService.releaseStock(request);
        } catch (Exception ex) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null,
                    "Failed to release stock: " + ex.getMessage());
        }
    }

    @GetMapping("/getStock")
    public APIResponse<InventoryDTO> getStock(
            @RequestParam Long tenantId,
            @RequestParam String productId,
            @RequestParam(required = false) Long warehouseId
    ) {
        try {
            return inventoryService.getStock(tenantId, productId, warehouseId);
        } catch (Exception ex) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null,
                    "Failed to fetch stock: " + ex.getMessage());
        }
    }
}
