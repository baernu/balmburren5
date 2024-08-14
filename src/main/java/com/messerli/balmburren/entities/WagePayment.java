package com.messerli.balmburren.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="wage_payment")
public class WagePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version @GeneratedValue(strategy = GenerationType.AUTO)
    private long version;
    @ManyToOne
    private User person;
    @ManyToOne
    private Invoice invoice;
    @ManyToOne
    private Dates dateFrom;
    @ManyToOne
    private Dates dateTo;
}
