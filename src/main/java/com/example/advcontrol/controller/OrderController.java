package com.example.advcontrol.controller;

import com.example.advcontrol.entity.Order;
import com.example.advcontrol.repository.OrderRepository;
import com.example.advcontrol.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    @GetMapping
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    @PutMapping("/{id}/validate")
    public Order validate(@PathVariable Long id) {
        return orderService.validate(id, "Manager");
    }
}
