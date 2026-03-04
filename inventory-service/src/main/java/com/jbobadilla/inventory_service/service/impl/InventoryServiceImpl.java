package com.jbobadilla.inventory_service.service.impl;

import com.jbobadilla.inventory_service.model.dto.Response;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;
import com.jbobadilla.inventory_service.model.entity.Inventory;
import com.jbobadilla.inventory_service.repository.InventoryRepository;
import com.jbobadilla.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User: jbobadilla
 * Date: 4/03/2026
 * Time: 01:18
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public boolean isInStock(String sku) {
        Optional<Inventory> inventory = inventoryRepository.findBySku(sku);
        return inventory.filter(value -> value.getQuantity() > 0).isPresent();
    }

    @Override
    public Response areInStock(List<OrderItemRequest> orderItems) {
        var errorList = new ArrayList<String>();

        List<String> skus = orderItems.stream().map(OrderItemRequest::getSku).toList();

        List<Inventory> inventoryList = inventoryRepository.findBySkuIn(skus);

        orderItems.forEach(orderItem -> {
            var inventory = inventoryList.stream().filter(value -> value.getSku().equals(orderItem.getSku())).findFirst();
            if (inventory.isEmpty()) {
                errorList.add("Product with SKU " + orderItem.getSku() + " does not exist");
            }  else if (inventory.get().getQuantity() < orderItem.getQuantity()) {
                errorList.add("Product with SKU " + orderItem.getSku() + " has insufficient quantity");
            }
        });
        return !errorList.isEmpty() ? new Response(errorList.toArray(new String[0])) : new Response(null);
    }
}