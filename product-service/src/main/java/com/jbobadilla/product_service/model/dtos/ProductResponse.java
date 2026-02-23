package com.jbobadilla.product_service.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponse {

    private Long id;
    private String sku;
    private String name;
    private String description;
    private Double price;
    private Boolean status;
}
