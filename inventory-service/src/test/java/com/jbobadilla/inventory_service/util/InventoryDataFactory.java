package com.jbobadilla.inventory_service.util;

import com.jbobadilla.inventory_service.model.dto.InventoryResponse;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;
import com.jbobadilla.inventory_service.model.entity.Inventory;

public class InventoryDataFactory {

    public static Inventory.InventoryBuilder getMockInventory() {
        return Inventory.builder()
                .sku("000001")
                .quantity(10L);
    }

    public static OrderItemRequest.OrderItemRequestBuilder getMockOrderItemRequest() {
        return OrderItemRequest.builder()
                .sku("000001")
                .quantity(10L);
    }

    public static InventoryResponse.InventoryResponseBuilder getMockInventoryResponse() {
        return InventoryResponse.builder()
                .sku("000001")
                .isInStock(true)
                .quantity(10L);
    }

}