package com.example.advcontrol.controller;
import com.example.advcontrol.entity.Product;
import com.example.advcontrol.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    @PostMapping("/seed")
    public List<Product> seedProducts() {
        if (productRepository.count() > 0) {
            return productRepository.findAll();
        }

        productRepository.save(create("BLCH8", "Brique Rouge 8", 1200, "BRIQUE"));
        productRepository.save(create("BLCH10", "Brique Rouge 10", 1400, "BRIQUE"));
        productRepository.save(create("BLCH12", "Brique Rouge 12", 1600, "BRIQUE"));
        productRepository.save(create("PB8", "Pavé Béton 8", 900, "PAVE"));
        productRepository.save(create("PB10", "Pavé Béton 10", 1100, "PAVE"));
        productRepository.save(create("PB12", "Pavé Béton 12", 1300, "PAVE"));

        return productRepository.findAll();
    }

    private Product create(String code, String name, double price, String category) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        product.setCategory(category);
        product.setActive(true);
        return product;
    }
}