package com.jbobadilla.order_service.model.dto;

import lombok.*;

import java.util.List;

/**
 * User: joalf
 * Date: 10/03/2026
 * Time: 17:13
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<OrderItemRequest> orderItems;
}