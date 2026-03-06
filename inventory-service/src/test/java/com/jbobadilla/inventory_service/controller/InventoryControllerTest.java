package com.jbobadilla.inventory_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbobadilla.inventory_service.model.dto.InventoryResponse;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;
import com.jbobadilla.inventory_service.service.InventoryService;
import com.jbobadilla.inventory_service.util.InventoryDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @MockitoBean
    private InventoryService inventoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderItemRequest orderItem;
    private InventoryResponse inventoryResponse;

    @BeforeEach
    void setUp() {
        orderItem = InventoryDataFactory.getMockOrderItemRequest().build();
        inventoryResponse = InventoryDataFactory.getMockInventoryResponse().build();
    }

    @Test
    @DisplayName("Is in stock")
    void isInStock() throws Exception {

        when(inventoryService.isInStock("000001")).thenReturn(true);

        mockMvc.perform(get("/api/inventory/{sku}", "000001"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(inventoryService).isInStock("000001");
    }

    @Test
    @DisplayName("Are in stock")
    void areInStock() throws Exception {
        when(inventoryService.areInStock(anyList())).thenReturn(List.of(inventoryResponse));

        mockMvc.perform(post("/api/inventory/in-stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(orderItem))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("000001"))
                .andExpect(jsonPath("$[0].inStock").value(true))
                .andExpect(jsonPath("$[0].quantity").value(10));

        verify(inventoryService).areInStock(anyList());
    }

    @Test
    @DisplayName("reduce stock")
    void reduceStock() throws Exception {
        doNothing().when(inventoryService).reduceStock(anyList());

        mockMvc.perform(put("/api/inventory/reduce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(orderItem))))
                .andExpect(status().isNoContent());

        verify(inventoryService).reduceStock(anyList());
    }
}