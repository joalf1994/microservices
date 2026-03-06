package com.jbobadilla.inventory_service.model.dto;

import lombok.Builder;
import lombok.Data;

/**
 * User: jbobadilla
 * Date: 4/03/2026
 * Time: 02:08
 */
@Data
@Builder
public class InventoryResponse {
    private String sku;
    private boolean isInStock;
    private Long quantity;
}