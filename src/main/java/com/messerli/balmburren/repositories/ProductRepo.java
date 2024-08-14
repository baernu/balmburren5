package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    boolean existsByName(String name);

    void delete(Optional<Product> product);
}
