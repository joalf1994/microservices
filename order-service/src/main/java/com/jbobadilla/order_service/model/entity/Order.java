package com.jbobadilla.order_service.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * User: jbobadilla
 * Date: 10/03/2026
 * Time: 12:46
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<OrderItem> orderItems;
}