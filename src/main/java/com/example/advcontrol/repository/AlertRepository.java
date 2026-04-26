package com.example.advcontrol.repository;

import com.example.advcontrol.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByOrderId(Long orderId);
}
