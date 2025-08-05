package com.shopsphere.catalog.serviceImpl;

import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.CreateCategoryRequest;
import com.shopsphere.catalog.entity.Category;
import com.shopsphere.catalog.repository.CategoryRepository;
import com.shopsphere.catalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public APIResponse addCategory(CreateCategoryRequest categoryRequest) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryRequest.getName());

            if (optionalCategory.isPresent()) {
                return APIResponse.builder()
                        .status(HttpStatus.CONFLICT)
                        .message("Category with name '" + categoryRequest.getName() + "' already exists")
                        .build();
            }

            Category category = Category.builder()
                    .name(categoryRequest.getName())
                    .slug(categoryRequest.getSlug())
                    .description(categoryRequest.getDescription())
                    .parentId(categoryRequest.getParentId())
                    .isActive(Boolean.TRUE)
                    .build();

            Category savedCategory = categoryRepository.save(category);

            return APIResponse.builder()
                    .status(HttpStatus.CREATED)
                    .message("Category added successfully")
                    .body(savedCategory)
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while adding category: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse getCategoryById(String id) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);

            if (optionalCategory.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Category with ID " + id + " not found")
                        .build();
            }

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Category fetched successfully")
                    .body(optionalCategory.get())
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error fetching category: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse getAllCategories(String categoryId, String subCategoryId) {
        try {
            List<Category> categories = categoryRepository.findAllByCategoryIdAndSubCategoryId(categoryId, subCategoryId);

            if (categories.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("No categories found !!")
                        .build();
            }

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Categories fetched successfully")
                    .body(categories)
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error fetching categories: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse updateCategory(String id, CreateCategoryRequest categoryRequest) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);

            if (optionalCategory.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Category with id '" + id + "' not found")
                        .build();
            }

            Category category = optionalCategory.get();

            if (categoryRequest.getName() != null)
                category.setName(categoryRequest.getName());

            if (categoryRequest.getSlug() != null)
                category.setSlug(categoryRequest.getSlug());

            if (categoryRequest.getDescription() != null)
                category.setDescription(categoryRequest.getDescription());

            if (categoryRequest.getParentId() != null)
                category.setParentId(categoryRequest.getParentId());

            Category updated = categoryRepository.save(category);

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Category updated partially")
                    .body(updated)
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error during partial update: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public APIResponse deleteCategory(String name) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByName(name);

            if (optionalCategory.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Category with name '" + name + "' not found")
                        .build();
            }

            Category category = optionalCategory.get();
            category.setIsActive(Boolean.FALSE);
            categoryRepository.save(category);

            return APIResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Category deleted successfully")
                    .build();

        } catch (Exception e) {
            return APIResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error deleting category: " + e.getMessage())
                    .build();
        }
    }
}
