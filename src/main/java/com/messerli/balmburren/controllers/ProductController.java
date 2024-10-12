package com.messerli.balmburren.controllers;


import com.messerli.balmburren.entities.ProductDetails;
import com.messerli.balmburren.exceptions.NoSuchElementFoundException;
import com.messerli.balmburren.services.ProductService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.messerli.balmburren.entities.Product;
import com.messerli.balmburren.entities.ProductBindProductDetails;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006","https://service.balmburren.net:8006","https://www.balmburren.net:4200"}
        , exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/pr/")
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("product/")
    ResponseEntity<Optional<Product>> createProduct(@RequestBody Product product) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/product").toUriString());
        return ResponseEntity.created(uri).body(productService.saveProduct(product));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/{name}")
    ResponseEntity<Optional<Product>> getProduct(@PathVariable("name") String name) {
        return ResponseEntity.ok().body(getProduct1(name));}

    private Optional<Product> getProduct1(String name) {
        Optional<Product> product = productService.getProduct(name);
        if (product.isEmpty()) throw new NoSuchElementFoundException("Product not found");
        return product;
    }


    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/")
    ResponseEntity<Optional<List<Product>>> getProducts() {
        return ResponseEntity.ok().body(productService.getProducts());}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping("product/")
    ResponseEntity<Optional<Product>> deleteProduct(@RequestBody Product product) {
        return ResponseEntity.ok().body(productService.deleteProduct(product));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping ("product/exist/{name}")
    ResponseEntity<Boolean> existProduct(@PathVariable("name") String name) {
        boolean bool = productService.existProduct(name);
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("product/details/")
    ResponseEntity<Optional<ProductDetails>> createProductDetails(@RequestBody ProductDetails productDetails) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/product/details").toUriString());
        return ResponseEntity.created(uri).body(productService.saveProductDetails(productDetails));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("product/details/{id}")
    ResponseEntity<Optional<ProductDetails>> putProductDetails(@RequestBody ProductDetails productDetails) {
        Optional<ProductDetails> productDetails1 = productService.putProductDetails(productDetails);
        if (productDetails1.isEmpty()) throw new NoSuchElementFoundException("ProductDetail not found");
        return ResponseEntity.ok().body(productDetails1);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/details/{id}")
    ResponseEntity<Optional<ProductDetails>> getProductDetails(@PathVariable("id") Long id) {
        Optional<ProductDetails> productDetails = productService.getProductDetails(id);
        if (productDetails.isEmpty()) throw new NoSuchElementFoundException("ProductDetail not found");
        return ResponseEntity.ok().body(productDetails);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/details/{category}")
    ResponseEntity<Optional<List<ProductDetails>>> getAllProductDetailsForProduct(@PathVariable("category") String category) {
        Optional<List<ProductDetails>> list = productService.getAllProductDetailsForCategory(category);
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/details/")
    ResponseEntity<Optional<List<ProductDetails>>> getAllProductDetails() {
        Optional<List<ProductDetails>> list = productService.getAllProductDetails();
        return ResponseEntity.ok().body(list);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping("product/details/")
    ResponseEntity<Optional<ProductDetails>> deleteProductDetails(@RequestBody ProductDetails productDetails) {
        return ResponseEntity.ok().body(productService.deleteProductDetails(productDetails));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("product/bind/infos/")
    ResponseEntity<Optional<ProductBindProductDetails>> createProductDetails(@RequestBody ProductBindProductDetails productBindInfos) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/product/bind/infos").toUriString());
        return ResponseEntity.created(uri).body(productService.saveProductBindInfos(productBindInfos));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("product/bind/infos/")
    ResponseEntity<Optional<ProductBindProductDetails>> putProductDetails(@RequestBody ProductBindProductDetails productBindInfos) {
        return ResponseEntity.ok().body(productService.putProductBindInfos(productBindInfos));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/bind/infos/{product}/{productdetails}")
    ResponseEntity<Optional<ProductBindProductDetails>> getProductBindInfos(@PathVariable("product") String name, @PathVariable("productdetails") Long id) {
        return ResponseEntity.ok().body(getProductBindInfo(name, id, 1));}

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("product/bind/infos/ischecked/{bool}")
    ResponseEntity<List<ProductBindProductDetails>> getProductBindInfosisChecked(@PathVariable("bool") Boolean bool) {
        return ResponseEntity.ok().body(productService.getAllProductDetailsisChecked(bool));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/bind/infos/exist/{product}/{productdetails}")
    ResponseEntity<Boolean> isProductBindInfos(@PathVariable("product") String name, @PathVariable("productdetails") Long id) {
        ProductBindProductDetails productBindInfos = null;
        Optional<Product> product1 = productService.getProduct(name);
        if (product1.isEmpty()) throw new NoSuchElementFoundException("Product not found");
        Optional<ProductDetails> productDetails = productService.getProductDetails(id);
        if (productDetails.isEmpty()) throw new NoSuchElementFoundException("ProductDetails not found");
        boolean bool = productService.isProductBindInfos(product1.get(), productDetails.get());
        return ResponseEntity.ok().body(bool);}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/bind/infos/byid/{id}")
    ResponseEntity<Optional<ProductBindProductDetails>> getProductBindInfosById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productService.getProductBindInfosById(id));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping("product/bind/infos/")
    ResponseEntity<Optional<ProductBindProductDetails>> deleteProductBindInfos(@RequestBody ProductBindProductDetails productBindProductDetails) {
        return ResponseEntity.ok().body(productService.deleteProductBindInfos(productBindProductDetails));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/bind/infos/{product}")
    ResponseEntity<Optional<List<ProductBindProductDetails>>> getAllProductBindInfosForProduct(@PathVariable("product") String name) {
        Optional<Product> product = productService.getProduct(name);
        if (product.isEmpty()) throw new NoSuchElementFoundException("Product not found");
        return ResponseEntity.ok().body(productService.getAllProductBindInfosForProduct(product.get()));}

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER', 'USER','KATHY')")
    @GetMapping("product/bind/infos/")
    ResponseEntity<Optional<List<ProductBindProductDetails>>> getAllProductBindInfos() {
        return ResponseEntity.ok().body(productService.getAllProductBindInfos());}

    private Optional<ProductBindProductDetails> getProductBindInfo(String product, Long id, int i) {
        Optional<ProductBindProductDetails> productBindInfos = null;
        Optional<Product> product1 = productService.getProduct(product);
        if (product1.isEmpty()) throw new NoSuchElementFoundException("Product not found");
        Optional<ProductDetails> productDetails = productService.getProductDetails(id);
        if (productDetails.isEmpty()) throw new NoSuchElementFoundException("ProductDetails not found");
        if (i == 1) productBindInfos = productService.getProductBindInfos(product1.get(), productDetails.get());
//        if (i == 0) productBindInfos = productService.deleteProductBindInfos(product1.get(), productDetails.get());
        if (productBindInfos.isEmpty()) throw new NoSuchElementFoundException("ProductBindInfo not found");
        return productBindInfos;
    }

}
