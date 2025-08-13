package com.shopsphere.catalog.repository;

import com.shopsphere.catalog.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByTenantIdAndProductIdAndWarehouseId(Long tenantId, String productId, Long warehouseId);

    Optional<Inventory> findByTenantIdAndProductId(Long tenantId, String productId);
}
