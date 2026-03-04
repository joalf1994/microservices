package com.jbobadilla.inventory_service.utils;

import com.jbobadilla.inventory_service.model.entity.Inventory;
import com.jbobadilla.inventory_service.repository.InventoryRepository;
import com.jbobadilla.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: jbobadilla
 * Date: 4/03/2026
 * Time: 03:38
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository  inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading data...");
        if (inventoryRepository.findAll().isEmpty()) {
            inventoryRepository.saveAll(
                    List.of(Inventory.builder().sku("000001").quantity(10L).build(),
                            Inventory.builder().sku("000002").quantity(20L).build(),
                            Inventory.builder().sku("000003").quantity(30L).build(),
                            Inventory.builder().sku("000004").quantity(0L).build())
            );
        }
        log.info("Loading data finished");
    }
}