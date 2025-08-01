package com.shopsphere.catalog.repository;

import com.shopsphere.catalog.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsBySku(String sku);

    Optional<Product> findByTenantIdAndNameAndBrandAndAttributes(String tenantId, String name, String brand, Map<String, String> attributes);

    Optional<Product> findByIdAndTenantId(String id, String tenantId);

    Optional<Product> findByNameAndTenantId(String name, String tenantId);

    List<Product> findByTenantId(String tenantId);
}
