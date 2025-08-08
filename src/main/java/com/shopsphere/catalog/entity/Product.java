package com.shopsphere.catalog.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "products")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "idx_tenant_cat_brand", def = "{'tenantId': 1, 'categoryId': 1, 'brand': 1}"),
        @CompoundIndex(name = "idx_price_material", def = "{'price': 1, 'material': 1}"),
        @CompoundIndex(name = "idx_subCategory", def = "{'subCategoryId': 1}"),
        @CompoundIndex(name = "idx_stock", def = "{'stock': 1}"),
        @CompoundIndex(name = "idx_name", def = "{'name': 1}")
})
public class Product {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private String slug;
    private String description;
    private String brand;
    private String categoryId;
    private String subCategoryId;
    private BigDecimal price;
    private BigDecimal finalPrice;
    private DiscountInfo discount;
    private Integer stock;
    private String sku;
    private List<String> images;
    private Map<String, String> attributes;
    private List<String> tags;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DiscountInfo {
        private String type;   // FLAT or PERCENT
        private Double value;
    }
}
