package com.jbobadilla.order_service.model.dto;

import com.jbobadilla.order_service.model.entity.OrderItem;
import lombok.*;

import java.util.List;

/**
 * User: joalf
 * Date: 10/03/2026
 * Time: 17:10
 */

public record OrderResponse (
        Long id,
        String orderNumber,
        List<OrderItemResponse> orderItems) {
}