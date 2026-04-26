package com.example.advcontrol.entity;

import com.example.advcontrol.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private String clientName;
    private String city;
    private String transportMode;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.BROUILLON;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private String createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();

    private String validatedBy;
    private LocalDateTime validatedAt;

    private String comment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;
}
