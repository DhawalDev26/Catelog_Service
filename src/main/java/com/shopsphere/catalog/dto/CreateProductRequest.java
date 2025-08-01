package com.shopsphere.catalog.dto;

import com.shopsphere.catalog.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    private String name;
    private String tenantId;
    private String description;
    private String brand;
    private String categoryId;
    private String subCategoryId;
    private BigDecimal price;
    private Integer stock;
    private String sku;
    private String slug;
    private List<String> images;
    private Map<String, String> attributes;
    private List<String> tags;
    private DiscountInfo discount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DiscountInfo {
        private String type;
        private Double value;
    }
}
