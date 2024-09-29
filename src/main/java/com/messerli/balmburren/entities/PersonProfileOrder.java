package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_profile_order")
public class PersonProfileOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    @Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;
    @ManyToOne
    private User user;
    @ManyToOne
    private ProductBindProductDetails productBindProductDetails;
    @ManyToOne
    private Tour tour;
    private Integer firstWeekOrder;
    private Integer secondWeekOrder;
}
