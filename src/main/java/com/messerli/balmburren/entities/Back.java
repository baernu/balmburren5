package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="back")
public class Back {
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
    private int quantity;

}
