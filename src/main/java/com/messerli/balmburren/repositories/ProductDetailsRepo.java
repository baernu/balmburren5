package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProductDetailsRepo extends JpaRepository<ProductDetails, Long> {
    Optional<ProductDetails> findById(Long id);
    Optional<List<ProductDetails>> findAllByCategory(String category);
}
