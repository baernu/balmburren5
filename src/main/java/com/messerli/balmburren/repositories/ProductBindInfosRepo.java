package com.messerli.balmburren.repositories;

import com.messerli.balmburren.entities.Product;
import com.messerli.balmburren.entities.ProductBindProductDetails;
import com.messerli.balmburren.entities.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductBindInfosRepo extends JpaRepository<ProductBindProductDetails, Long> {
    Optional<ProductBindProductDetails> findByProductAndProductDetails(Product product, ProductDetails productDetails);
    boolean existsByProductAndProductDetails(Product product, ProductDetails productDetails);
    Optional<List<ProductBindProductDetails>> findAllByProduct(Product product);
    Optional<ProductBindProductDetails> findById(Long id);
    List<ProductBindProductDetails> findAllByIsChecked(boolean bool);

}
