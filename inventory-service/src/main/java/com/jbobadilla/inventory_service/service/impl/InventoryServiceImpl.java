package com.jbobadilla.inventory_service.service.impl;

import com.jbobadilla.inventory_service.exception.InsufficientStockException;
import com.jbobadilla.inventory_service.exception.ProductNotFoundException;
import com.jbobadilla.inventory_service.model.dto.InventoryResponse;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;
import com.jbobadilla.inventory_service.model.entity.Inventory;
import com.jbobadilla.inventory_service.model.mapper.InventoryMapper;
import com.jbobadilla.inventory_service.repository.InventoryRepository;
import com.jbobadilla.inventory_service.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User: jbobadilla
 * Date: 4/03/2026
 * Time: 01:18
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public boolean isInStock(String sku) {
        Optional<Inventory> inventory = inventoryRepository.findBySku(sku);
        return inventory.filter(value -> value.getQuantity() > 0).isPresent();
    }

    @Override
    public List<InventoryResponse> areInStock(List<OrderItemRequest> orderItems) {
        List<InventoryResponse> responseList = new ArrayList<>();
        var skus = orderItems.stream().map(OrderItemRequest::getSku).toList();

        List<Inventory> inventoryLit = inventoryRepository.findBySkuIn(skus);
        Map<String, Inventory> inventoryMap = inventoryLit.stream().collect(Collectors.toMap(Inventory::getSku, Function.identity()));

        orderItems.forEach(orderItem -> {
            Inventory inventory = inventoryMap.get(orderItem.getSku());

            boolean inStock = inventory != null && inventory.getQuantity() >= orderItem.getQuantity();
            long inStockQuantity = inventory != null ? inventory.getQuantity() : 0L;

            responseList.add(
                    InventoryResponse.builder()
                    .sku(orderItem.getSku())
                    .isInStock(inStock)
                    .quantity(inStockQuantity)
                    .build());
        });
        return responseList;
    }

    @Override
    @Transactional
    public void reduceStock(List<OrderItemRequest> orderItems) {

        var sks = orderItems.stream().map(OrderItemRequest::getSku).toList();

        List<Inventory> inventoryLit = inventoryRepository.findBySkuIn(sks);
        Map<String, Inventory> inventoryMap = inventoryLit.stream().collect(Collectors.toMap(Inventory::getSku, Function.identity()));

        orderItems.forEach(orderItem -> {
            Inventory inventory = inventoryMap.get(orderItem.getSku());
            if (inventory == null) {
                throw new ProductNotFoundException("Product not found: " + orderItem.getSku());
            }

            long reduce = inventory.getQuantity() - orderItem.getQuantity();

            if (reduce < 0) {
                throw new InsufficientStockException("Insufficient stock for: " + orderItem.getSku());
            }

            inventory.setQuantity(reduce);
        });
    }
}