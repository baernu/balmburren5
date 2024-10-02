package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ordered")
public class Ordered {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Version
    @Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;
    @ManyToOne
    private User deliverPeople;
    @ManyToOne
    private Dates date;
    @ManyToOne
    private ProductBindProductDetails productBindInfos;
    @ManyToOne
    private Tour tour;
    private int quantityOrdered;
    private int quantityDelivered;
    private Boolean isChecked = false;
    private String text;
}
