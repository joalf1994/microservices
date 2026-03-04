package com.jbobadilla.inventory_service.service;

import com.jbobadilla.inventory_service.model.dto.Response;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;

import java.util.List;

public interface InventoryService {

    boolean isInStock(String sku);

    Response areInStock(List<OrderItemRequest> orderItems);

}
