package com.jbobadilla.inventory_service.repository;

import com.jbobadilla.inventory_service.model.entity.Inventory;
import com.jbobadilla.inventory_service.util.InventoryDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    private Inventory inventory;

    @BeforeEach
    public void setup() {
        inventory = InventoryDataFactory.getMockInventory().build();
    }

    @Test
    @DisplayName("Should return optional inventory when sku exists")
    void shouldReturnOptionalInventoryWhenSkuExists() {
        inventoryRepository.save(inventory);

        Optional<Inventory> inventoryDb = inventoryRepository.findBySku("000001");

        assertThat(inventoryDb).isNotNull();
        assertThat(inventoryDb.isPresent()).isTrue();
        assertThat(inventoryDb.get().getSku()).isEqualTo("000001");
    }

    @Test
    @DisplayName("Should return list inventory when skus list exixts")
    void shouldReturnListInventoryWhenSkusListExixts() {
        Inventory inventory2 = Inventory.builder()
                .sku("000002")
                .quantity(20L)
                .build();
        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory2);

        List<Inventory> inventories = inventoryRepository.findBySkuIn(List.of("000001", "000002"));

        assertThat(inventories).isNotNull();
        assertThat(inventories.size()).isEqualTo(2);

    }




}