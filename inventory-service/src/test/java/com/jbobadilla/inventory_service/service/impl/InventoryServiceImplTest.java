package com.jbobadilla.inventory_service.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.jbobadilla.inventory_service.exception.InsufficientStockException;
import com.jbobadilla.inventory_service.exception.ProductNotFoundException;
import com.jbobadilla.inventory_service.model.dto.InventoryResponse;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;
import com.jbobadilla.inventory_service.model.entity.Inventory;
import com.jbobadilla.inventory_service.repository.InventoryRepository;
import com.jbobadilla.inventory_service.util.InventoryDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory inventory;
    private OrderItemRequest request;

    @BeforeEach
    void setUp() {
        inventory = InventoryDataFactory.getMockInventory().id(1L).build();
        request = InventoryDataFactory.getMockOrderItemRequest().build();
    }

    /* ---------------- isInStock ----------------*/

    @Test
    @DisplayName("Should return true when product has stock")
    void shouldReturnTrueWhenProductHasStock() {
        when(inventoryRepository.findBySku("000001"))
                .thenReturn(Optional.of(inventory));

        boolean result = inventoryService.isInStock("000001");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when product has zero stock")
    void shouldReturnFalseWhenProductHasZeroStock() {
        inventory.setQuantity(0L);

        when(inventoryRepository.findBySku("000001"))
                .thenReturn(Optional.of(inventory));

        boolean result = inventoryService.isInStock("000001");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false when product does not exist")
    void shouldReturnFalseWhenProductDoesNotExist() {
        when(inventoryRepository.findBySku("000001"))
                .thenReturn(Optional.empty());

        boolean result = inventoryService.isInStock("000001");

        assertThat(result).isFalse();
    }

    /* ---------------- areInStock ----------------*/

    @Test
    @DisplayName("Should return product with stock")
    void shouldReturnProductWithStock() {

        when(inventoryRepository.findBySkuIn(List.of("000001")))
                .thenReturn(List.of(inventory));

        List<InventoryResponse> result = inventoryService.areInStock(List.of(request));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isInStock()).isTrue();
        assertThat(result.get(0).getQuantity()).isEqualTo(request.getQuantity());
    }

    @Test
    @DisplayName("Should return false when stock is insufficient")
    void shouldReturnFalseWhenStockIsInsufficient() {
        request.setQuantity(20L);
        when(inventoryRepository.findBySkuIn(List.of("000001")))
                .thenReturn(List.of(inventory));

        List<InventoryResponse> result = inventoryService.areInStock(List.of(request));

        assertThat(result.get(0).isInStock()).isFalse();
    }

    @Test
    @DisplayName("Should return zero quantity when product does not exist")
    void shouldReturnZeroQuantityWhenProductDoesNotExist() {
        when(inventoryRepository.findBySkuIn(List.of("000001")))
                .thenReturn(List.of());

        List<InventoryResponse> result = inventoryService.areInStock(List.of(request));

        assertThat(result.get(0).isInStock()).isFalse();
        assertThat(result.get(0).getQuantity()).isEqualTo(0L);
    }

    /* ---------------- reduceStock ----------------*/

    @Test
    @DisplayName("Should reduce stock successfully")
    void shouldReduceStockSuccessfully() {
        when(inventoryRepository.findBySkuIn(List.of("000001")))
                .thenReturn(List.of(inventory));

        inventoryService.reduceStock(List.of(request));

        assertThat(inventory.getQuantity()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void shouldThrowExceptionWhenProductNotFound() {
        when(inventoryRepository.findBySkuIn(List.of("000001"))).thenReturn(List.of());

        assertThatThrownBy(() -> inventoryService.reduceStock(List.of(request)))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found: " + request.getSku());

        verify(inventoryRepository).findBySkuIn(List.of("000001"));
    }

    @Test
    @DisplayName("Should throw exception when stock is insufficient")
    void shouldThrowExceptionWhenStockIsInsufficient() {
        request.setQuantity(20L);

        when(inventoryRepository.findBySkuIn(List.of("000001"))).thenReturn(List.of(inventory));

        assertThatThrownBy(() -> inventoryService.reduceStock(List.of(request)))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessage("Insufficient stock for: " + request.getSku());

        verify(inventoryRepository).findBySkuIn(List.of("000001"));
    }
}