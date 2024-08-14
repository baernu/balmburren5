package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="person_profile_order")
public class PersonProfileOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private Long version;
    @ManyToOne
    private User person;
    @ManyToOne
    private ProductBindProductDetails productBindProductDetails;
    @ManyToOne
    private Tour tour;
    private Integer firstWeekOrder;
    private Integer secondWeekOrder;
}
