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
@Setter
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;

    private Long quantity;
}