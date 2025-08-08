package com.shopsphere.catalog.service;

import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.CreateProductRequest;

public interface ProductService {
    APIResponse addProduct(CreateProductRequest productRequest);

    APIResponse getProductById(String id);
    
    APIResponse updateProduct(String id, CreateProductRequest productRequest);

    APIResponse deleteProduct(String id, String tenantId);

    APIResponse getAllProducts(String tenantId, String categoryId, String subCategoryId, String brand, String name, Double minPrice, Double maxPrice, Boolean inStock);

    APIResponse updateProductStatus(String id, Boolean status);

    APIResponse updateProductStocks(String id, Integer stocks);
}
