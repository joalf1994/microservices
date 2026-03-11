package com.jbobadilla.order_service.model.dto;

public record OrderItemResponse(
        Long id,
        String sku,
        Double price,
        Long quantity) {
}
