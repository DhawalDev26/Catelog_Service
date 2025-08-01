package com.shopsphere.catalog.service;

import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.CreateCategoryRequest;

public interface CategoryService {
    APIResponse addCategory(CreateCategoryRequest categoryRequest);

    APIResponse getCategoryById(String id);

    APIResponse getAllCategories();

    APIResponse updateCategory(String id, CreateCategoryRequest categoryRequest);

    APIResponse deleteCategory(String name);
}
