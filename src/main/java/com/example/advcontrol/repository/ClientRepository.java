package com.example.advcontrol.repository;

import com.example.advcontrol.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByActiveTrue();
}