package com.shopsphere.catalog.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {

    private String tenantId;
    private String name;
    private String slug;
    private String description;
    private String parentId;
}

