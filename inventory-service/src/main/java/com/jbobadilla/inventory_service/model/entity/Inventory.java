package com.jbobadilla.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * User: jbobadilla
 * Date: 4/03/2026
 * Time: 01:12
 */

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sku;

    private int quantity;
}