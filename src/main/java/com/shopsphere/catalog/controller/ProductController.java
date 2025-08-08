package com.shopsphere.catalog.controller;

import com.shopsphere.catalog.dto.APIResponse;
import com.shopsphere.catalog.dto.CreateProductRequest;
import com.shopsphere.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public APIResponse addProduct(@RequestBody CreateProductRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    @GetMapping("/getProductById")
    public APIResponse getProductById(@RequestParam String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/getAllProducts")
    public APIResponse getAllProducts(@RequestParam(required = false) String tenantId,
                                      @RequestParam(required = false) String categoryId,
                                      @RequestParam(required = false) String subCategoryId,
                                      @RequestParam(required = false) String brand,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Double minPrice,
                                      @RequestParam(required = false) Double maxPrice,
                                      @RequestParam(required = false) Boolean inStock) {
        return productService.getAllProducts(tenantId, categoryId, subCategoryId, brand, name, minPrice, maxPrice, inStock);
    }

    @PatchMapping("/update")
    public APIResponse updateProduct(@RequestParam String id,
                                     @RequestBody CreateProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @PatchMapping("/updateProductStatus")
    public APIResponse updateProductStatus(@RequestParam String id,@RequestParam Boolean status) {
        return productService.updateProductStatus(id, status);
    }

    @PatchMapping("/updateStocks")
    public APIResponse updateProductStocks(@RequestParam String id,
                                     @RequestParam Integer stocks) {
        return productService.updateProductStocks(id, stocks);
    }

    @DeleteMapping("/delete")
    public APIResponse deleteProduct(@RequestParam String id, @RequestParam String tenantId) {
        return productService.deleteProduct(id, tenantId);
    }
}
