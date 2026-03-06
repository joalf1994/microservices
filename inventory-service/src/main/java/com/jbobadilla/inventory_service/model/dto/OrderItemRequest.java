package com.jbobadilla.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: jbobadilla
 * Date: 4/03/2026
 * Time: 02:01
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {
    private String sku;
    private Long quantity;

}