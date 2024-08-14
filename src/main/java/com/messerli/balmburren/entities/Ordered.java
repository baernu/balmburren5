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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private Long version;
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
