package com.jbobadilla.inventory_service.controller;

import com.jbobadilla.inventory_service.model.dto.InventoryResponse;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;
import com.jbobadilla.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User: jbobadillla
 * Date: 4/03/2026
 * Time: 01:20
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{sku}")
    public ResponseEntity<Boolean> isInStock(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.isInStock(sku));
    }

    @PostMapping("/in-stock")
    public ResponseEntity<List<InventoryResponse>> areInStock(@RequestBody List<OrderItemRequest> orderItems) {
        return ResponseEntity.ok(inventoryService.areInStock(orderItems));
    }

    @PutMapping("/reduce")
    public ResponseEntity<Void> reduceStock(@RequestBody List<OrderItemRequest> orderItems) {
        inventoryService.reduceStock(orderItems);
        return ResponseEntity.noContent().build();
    }

}