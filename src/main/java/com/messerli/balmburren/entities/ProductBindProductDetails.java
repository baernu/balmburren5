package com.messerli.balmburren.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="product_bind_product_details")
public class ProductBindProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Version
    @Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;
    @ManyToOne
    private Product product;
    @ManyToOne
    private ProductDetails productDetails;
    @ManyToOne
    private Dates startDate;
    @ManyToOne
    private Dates endDate;
    private Boolean isChecked = false;

}
