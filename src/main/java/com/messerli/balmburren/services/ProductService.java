package com.messerli.balmburren.services;

import com.messerli.balmburren.entities.Product;
import com.messerli.balmburren.entities.ProductBindProductDetails;
import com.messerli.balmburren.entities.ProductDetails;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> saveProduct(Product product);
    Optional<Product> getProduct(String name);
    Optional<Product> deleteProduct(String name);
    boolean existProduct(String name);
    Optional<List<Product>> getProducts();
    Optional<ProductDetails> saveProductDetails(ProductDetails productDetails);
    Optional<ProductDetails> getProductDetails(Long id);
    Optional<List<ProductDetails>> getAllProductDetailsForCategory(String category);
    Optional<List<ProductDetails>> getAllProductDetails();



    Optional<ProductDetails> deleteProductDetails(ProductDetails productDetails);

    Optional<Product> deleteProduct(Product product);

    Optional<ProductDetails> putProductDetails(ProductDetails productDetails);
    Optional<ProductBindProductDetails> saveProductBindInfos(ProductBindProductDetails productBindInfos);
    Optional<ProductBindProductDetails> putProductBindInfos(ProductBindProductDetails productBindInfos);
    Optional<ProductBindProductDetails> getProductBindInfos(Product product, ProductDetails productDetails);
    boolean isProductBindInfos(Product product, ProductDetails productDetails);
    Optional<ProductBindProductDetails> getProductBindInfosById(Long id);
    Optional<ProductBindProductDetails> deleteProductBindInfos(ProductBindProductDetails productBindProductDetails);
    Optional<List<ProductBindProductDetails>> getAllProductBindInfosForProduct(Product product);
    Optional<List<ProductBindProductDetails>> getAllProductBindInfos();
}
