package com.shopsphere.catalog.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDiscountRequest {

    private String type;
    private Double value;

    private String appliesTo;
    private String targetId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
