package com.jbobadilla.inventory_service.model.mapper;

import com.jbobadilla.inventory_service.model.dto.InventoryResponse;
import com.jbobadilla.inventory_service.model.dto.OrderItemRequest;
import com.jbobadilla.inventory_service.model.entity.Inventory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    Inventory toEntity(OrderItemRequest orderItem);

    List<Inventory> toEntity(List<OrderItemRequest> orderItems);
}