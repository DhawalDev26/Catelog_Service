package com.shopsphere.catalog.controller;

import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.CreateCategoryRequest;
import com.shopsphere.catalog.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/addcategory")
    public APIResponse addCategory(@RequestBody CreateCategoryRequest categoryRequest) {
        logger.info("add category api called");
        return categoryService.addCategory(categoryRequest);
    }

    @GetMapping("/getbyid")
    public APIResponse getCategoryById(@RequestParam String id) {
        logger.info("get category by id api called");
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/getallcategories")
    public APIResponse getAllCategories() {
        logger.info("getAllCategories API called!!");
        return categoryService.getAllCategories();
    }

    @PatchMapping("/update")
    public APIResponse updateCategory(@RequestParam String id, @RequestBody CreateCategoryRequest categoryRequest) {
        logger.info("update category by id api called");
        return categoryService.updateCategory(id, categoryRequest);
    }

    @PostMapping("/delete")
    public APIResponse deleteCategory(@RequestParam String name) {
        logger.info("delete category  by email api called");
        return categoryService.deleteCategory(name);
    }
}
