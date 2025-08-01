package com.shopsphere.catalog.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    private String id;
    private String name;
    private String slug;
    private String description;

    private String parentId;

    private Boolean isActive;
}

