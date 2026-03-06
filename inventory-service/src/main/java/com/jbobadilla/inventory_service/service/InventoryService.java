package com.jbobadilla.inventory_service.service;

import com.jbobadilla.inventory_service.model.dto.InventoryResponse;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;

import java.util.List;

public interface InventoryService {

    boolean isInStock(String sku);

    List<InventoryResponse> areInStock(List<OrderItemRequest> orderItems);

    void reduceStock(List<OrderItemRequest> orderItems);

}
