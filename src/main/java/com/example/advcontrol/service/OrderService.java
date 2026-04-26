package com.example.advcontrol.service;

import com.example.advcontrol.entity.Alert;
import com.example.advcontrol.entity.Order;
import com.example.advcontrol.entity.OrderItem;
import com.example.advcontrol.enums.AlertLevel;
import com.example.advcontrol.enums.OrderStatus;
import com.example.advcontrol.repository.AlertRepository;
import com.example.advcontrol.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final AlertRepository alertRepository;

    private final List<String> allowedArticles = List.of(
            "BLCH8", "BLCH10", "BLCH12",
            "PB8", "PB10", "PB12"
    );

    public Order create(Order order) {
        order.setReference("CMD-" + System.currentTimeMillis());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            item.setOrder(order);

            BigDecimal lineTotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            item.setTotalPrice(lineTotal);
            total = total.add(lineTotal);
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);

        checkOrder(saved);

        return saved;
    }

    public void checkOrder(Order order) {
        if (order.getClientName() == null || order.getClientName().isBlank()) {
            createAlert(order, "CLIENT", "Client obligatoire", AlertLevel.HIGH);
        }

        if (order.getCity() == null || order.getCity().isBlank()) {
            createAlert(order, "VILLE", "Ville de livraison obligatoire", AlertLevel.MEDIUM);
        }

        for (OrderItem item : order.getItems()) {
            if (!allowedArticles.contains(item.getArticleCode())) {
                createAlert(order, "ARTICLE", "Article non autorisé : " + item.getArticleCode(), AlertLevel.HIGH);
                item.setValid(false);
            }
        }

        order.setStatus(OrderStatus.EN_VERIFICATION);
        orderRepository.save(order);
    }

    public Order validate(Long id, String managerName) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        order.setStatus(OrderStatus.PRETE_SAGE);
        order.setValidatedBy(managerName);
        order.setValidatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    private void createAlert(Order order, String type, String message, AlertLevel level) {
        Alert alert = new Alert();
        alert.setOrder(order);
        alert.setType(type);
        alert.setMessage(message);
        alert.setLevel(level);
        alertRepository.save(alert);
    }
}
