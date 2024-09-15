package com.messerli.balmburren.services.serviceImpl;


import com.messerli.balmburren.entities.Product;
import com.messerli.balmburren.repositories.ProductBindInfosRepo;
import com.messerli.balmburren.repositories.ProductDetailsRepo;
import com.messerli.balmburren.repositories.ProductRepo;
import com.messerli.balmburren.services.ProductService;
import com.messerli.balmburren.entities.ProductDetails;
import com.messerli.balmburren.entities.ProductBindProductDetails;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductDetailsRepo productDetailsRepo;
    private final ProductBindInfosRepo productBindInfosRepo;

    public ProductServiceImpl(ProductRepo productRepo, ProductDetailsRepo productDetailsRepo, ProductBindInfosRepo productBindInfosRepo) {
        this.productRepo = productRepo;
        this.productDetailsRepo = productDetailsRepo;
        this.productBindInfosRepo = productBindInfosRepo;
    }

    @Override
    public Optional<Product> saveProduct(Product product) {
        log.info("Saving new Product: {} to the database", product.getName());
        return Optional.of(productRepo.save(product));
    }

    @Override
    public Optional<Product> getProduct(String name) {
        Optional<Product> product = productRepo.findByName(name);
        log.info("Get Product with name: {}", name);
        return product;
    }

    @Override
    public Optional<Product> deleteProduct(String name) {
        Optional<Product> product = getProduct(name);
        log.info("Deleted Product with name: {}", product.get().getName());
        productRepo.delete(product.get());
        return product;
    }

    @Override
    public boolean existProduct(String name) {
        boolean isExist = productRepo.existsByName(name);
        log.info("Tour exist: {}", isExist);
        return isExist;
    }

    @Override
    public Optional<List<Product>> getProducts() {
        log.info("Get all available products");
        return Optional.of(productRepo.findAll());
    }

    @Override
    public Optional<ProductDetails> saveProductDetails(ProductDetails productDetails) {
        log.info("Saving new ProductDetails with description: {} to the database", productDetails.getDescription());
        return Optional.of(productDetailsRepo.save(productDetails));
    }

    @Override
    public Optional<ProductDetails> getProductDetails(Long id) {
        Optional<ProductDetails> productDetails = productDetailsRepo.findById(id);
        log.info("Get the ProductDetails with the descripion: {}", productDetails.get().getDescription());
        return productDetails;
    }

    @Override
    public Optional<List<ProductDetails>> getAllProductDetailsForCategory(String category) {
        Optional<List<ProductDetails>> list = productDetailsRepo.findAllByCategory(category);
        log.info("Get all ProductDetails: {} for Category: {}", list.get(), category);
        return list;
    }

    @Override
    public Optional<List<ProductDetails>> getAllProductDetails() {
        List<ProductDetails> list = productDetailsRepo.findAll();
        log.info("Get all ProductDetails: {}", list);
        return Optional.of(list);
    }

    @Override
    public Optional<ProductDetails> deleteProductDetails(ProductDetails productDetails) {
        productDetailsRepo.delete(productDetails);
        log.info("Deleted ProductDetails with the description: {}", productDetails.getDescription());
        return Optional.of(productDetails);
    }

    @Override
    public Optional<Product> deleteProduct(Product product) {
        productRepo.delete(product);
        log.info("Deleted Product {}", product);
        return Optional.of(product);
    }


    @Override
    public Optional<ProductDetails> putProductDetails(ProductDetails productDetails) {
        log.info("Put ProductDetails: {}", productDetails);
        return Optional.of(productDetailsRepo.save(productDetails));
    }

    @Override
    public Optional<ProductBindProductDetails> saveProductBindInfos(ProductBindProductDetails productBindInfos) {
        log.info("Saving ProductBindInfos: {}", productBindInfos);
        return Optional.of(productBindInfosRepo.save(productBindInfos));
    }

    @Override
    public Optional<ProductBindProductDetails> putProductBindInfos(ProductBindProductDetails productBindInfos) {
        log.info("Putting ProductBindInfos: {}", productBindInfos);
        return Optional.of(productBindInfosRepo.save(productBindInfos));
    }

    @Override
    public Optional<ProductBindProductDetails> getProductBindInfos(Product product, ProductDetails productDetails) {
        Optional<ProductBindProductDetails> productBindInfos = productBindInfosRepo.findByProductAndProductDetails(product, productDetails);
        log.info("Gets ProductInfos: {}", productBindInfos.get());
        return productBindInfos;
    }

    @Override
    public boolean isProductBindInfos(Product product, ProductDetails productDetails) {
        boolean isExist = productBindInfosRepo.existsByProductAndProductDetails(product, productDetails);
        log.info("is ProductBindInfos: {}", isExist);
        return isExist;
    }

    @Override
    public Optional<ProductBindProductDetails> getProductBindInfosById(Long id) {
        Optional<ProductBindProductDetails> productBindInfos = productBindInfosRepo.findById(id);
        log.info("Gets ProductInfos: {}", productBindInfos);
        return productBindInfos;
    }

    @Override
    public Optional<ProductBindProductDetails> deleteProductBindInfos(ProductBindProductDetails productBindProductDetails) {
        log.info("Deleted ProductBindProductDetails : {} ", productBindProductDetails);
        productBindInfosRepo.delete(productBindProductDetails);
        return Optional.of(productBindProductDetails);
    }

    @Override
    public Optional<List<ProductBindProductDetails>> getAllProductBindInfosForProduct(Product product) {
        log.info("Get all ProductBindInfos for the product with name: {}", product.getName());
        return productBindInfosRepo.findAllByProduct(product);
    }

    @Override
    public Optional<List<ProductBindProductDetails>> getAllProductBindInfos() {
        log.info("Get all ProductBindInfos");
        return Optional.of(productBindInfosRepo.findAll());
    }
}
