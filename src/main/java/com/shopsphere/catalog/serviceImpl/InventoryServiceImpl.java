package com.shopsphere.catalog.serviceImpl;// package com.shopespher.inventory.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.InventoryDTO;
import com.shopsphere.catalog.dto.StockRequest;
import com.shopsphere.catalog.dto.StockReservationRequest;
import com.shopsphere.catalog.entity.Inventory;
import com.shopsphere.catalog.exception.InventoryException;
import com.shopsphere.catalog.repository.InventoryRepository;
import com.shopsphere.catalog.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public APIResponse<InventoryDTO> addStock(StockRequest request) {
        try {
            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                throw new InventoryException("Quantity must be positive for addStock");
            }

            Inventory inventory = inventoryRepository
                    .findByTenantIdAndProductId(
                            request.getTenantId(), request.getProductId()
                    ).orElseGet(() -> Inventory.builder()
                            .tenantId(request.getTenantId())
                            .productId(request.getProductId())
                            .availableQuantity(0)
                            .reservedQuantity(0)
                            .safetyStock(0)
                            .build()
                    );

            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + request.getQuantity());
            Inventory saved = inventoryRepository.save(inventory);

            InventoryDTO dto = objectMapper.convertValue(saved, InventoryDTO.class);
            return new APIResponse<>(HttpStatus.OK, dto, "Stock added successfully");

        } catch (InventoryException ie) {
            throw ie;
        } catch (Exception ex) {
            throw new InventoryException("Failed to add stock: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public APIResponse<InventoryDTO> removeStock(StockRequest request) {
        try {
            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                throw new InventoryException("Quantity must be positive for removeStock");
            }

            Inventory inventory = inventoryRepository
                    .findByTenantIdAndProductId(
                            request.getTenantId(), request.getProductId()
                    ).orElseThrow(() -> new InventoryException("Inventory record not found"));

            if (inventory.getAvailableQuantity() < request.getQuantity()) {
                throw new InventoryException("Insufficient available quantity");
            }

            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - request.getQuantity());
            Inventory saved = inventoryRepository.save(inventory);

//            InventoryLog log = InventoryLog.builder()
//                    .inventoryId(saved.getId())
//                    .productId(saved.getProductId())
//                    .tenantId(saved.getTenantId())
//                    .changeType(ChangeType.REMOVE_STOCK)
//                    .quantity(request.getQuantity())
//                    .referenceId(request.getReferenceId())
//                    .note(request.getNote())
//                    .createdAt(Instant.now())
//                    .build();
//            inventoryLogRepository.save(log);

            InventoryDTO dto = objectMapper.convertValue(saved, InventoryDTO.class);
            return new APIResponse<>(HttpStatus.OK, dto, "Stock removed successfully");

        } catch (InventoryException ie) {
            throw ie;
        } catch (Exception ex) {
            throw new InventoryException("Failed to remove stock: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public APIResponse<InventoryDTO> reserveStock(StockReservationRequest request) {
        try {
            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                throw new InventoryException("Quantity must be positive for reserveStock");
            }

            Inventory inventory = inventoryRepository
                    .findByTenantIdAndProductIdAndWarehouseId(
                            request.getTenantId(), request.getProductId(), request.getWarehouseId()
                    ).orElseThrow(() -> new InventoryException("Inventory record not found"));

            if (inventory.getAvailableQuantity() < request.getQuantity()) {
                throw new InventoryException("Insufficient available quantity to reserve");
            }

            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - request.getQuantity());
            inventory.setReservedQuantity(inventory.getReservedQuantity() + request.getQuantity());
            Inventory saved = inventoryRepository.save(inventory);

//            InventoryLog log = InventoryLog.builder()
//                    .inventoryId(saved.getId())
//                    .productId(saved.getProductId())
//                    .tenantId(saved.getTenantId())
//                    .changeType(ChangeType.RESERVE_STOCK)
//                    .quantity(request.getQuantity())
//                    .referenceId(request.getReferenceId())
//                    .createdAt(Instant.now())
//                    .build();
//            inventoryLogRepository.save(log);

            InventoryDTO dto = objectMapper.convertValue(saved, InventoryDTO.class);
            return new APIResponse<>(HttpStatus.OK, dto, "Stock reserved successfully");

        } catch (InventoryException ie) {
            throw ie;
        } catch (Exception ex) {
            throw new InventoryException("Failed to reserve stock: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public APIResponse<InventoryDTO> releaseStock(StockReservationRequest request) {
        try {
            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                throw new InventoryException("Quantity must be positive for releaseStock");
            }

            Inventory inventory = inventoryRepository
                    .findByTenantIdAndProductIdAndWarehouseId(
                            request.getTenantId(), request.getProductId(), request.getWarehouseId()
                    ).orElseThrow(() -> new InventoryException("Inventory record not found"));

            if (inventory.getReservedQuantity() < request.getQuantity()) {
                throw new InventoryException("Insufficient reserved quantity to release");
            }

            inventory.setReservedQuantity(inventory.getReservedQuantity() - request.getQuantity());
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + request.getQuantity());
            Inventory saved = inventoryRepository.save(inventory);

//            InventoryLog log = InventoryLog.builder()
//                    .inventoryId(saved.getId())
//                    .productId(saved.getProductId())
//                    .tenantId(saved.getTenantId())
//                    .changeType(ChangeType.RELEASE_STOCK)
//                    .quantity(request.getQuantity())
//                    .referenceId(request.getReferenceId())
//                    .createdAt(Instant.now())
//                    .build();
//            inventoryLogRepository.save(log);

            InventoryDTO dto = objectMapper.convertValue(saved, InventoryDTO.class);
            return new APIResponse<>(HttpStatus.OK, dto, "Stock released successfully");

        } catch (InventoryException ie) {
            throw ie;
        } catch (Exception ex) {
            throw new InventoryException("Failed to release stock: " + ex.getMessage(), ex);
        }
    }

    @Transactional
    @Override
    public APIResponse<InventoryDTO> getStock(Long tenantId, String productId, Long warehouseId) {
        try {
            Inventory inventory = (warehouseId != null)
                    ? inventoryRepository.findByTenantIdAndProductIdAndWarehouseId(tenantId, productId, warehouseId)
                    .orElse(null)
                    : inventoryRepository.findByTenantIdAndProductId(tenantId, productId).orElse(null);

            if (inventory == null) {
                return new APIResponse<>(HttpStatus.NOT_FOUND, null, "Inventory not found");
            }

            InventoryDTO dto = objectMapper.convertValue(inventory, InventoryDTO.class);
            return new APIResponse<>(HttpStatus.OK, dto, "Inventory fetched");

        } catch (Exception ex) {
            throw new InventoryException("Failed to fetch inventory: " + ex.getMessage(), ex);
        }
    }
}
