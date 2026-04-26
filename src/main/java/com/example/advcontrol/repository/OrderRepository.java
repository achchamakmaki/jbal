package com.example.advcontrol.repository;

import com.example.advcontrol.entity.Order;
import com.example.advcontrol.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByStatus(OrderStatus status);
}
