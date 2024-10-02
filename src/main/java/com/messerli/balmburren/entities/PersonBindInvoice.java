package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_and_invoice")
public class PersonBindInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    @Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;
    @ManyToOne
    private User personDeliver;
    @ManyToOne
    private User personInvoice;
    @ManyToOne
    private Invoice invoice;
    @ManyToOne
    private Dates dateFrom;
    @ManyToOne
    private Dates dateTo;
    private Boolean isChecked = false;
}
