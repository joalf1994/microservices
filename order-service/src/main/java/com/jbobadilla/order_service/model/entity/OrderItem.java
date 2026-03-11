package com.jbobadilla.order_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * User: jbobadilla
 * Date: 10/03/2026
 * Time: 12:47
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sku;

    private Double price;

    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}