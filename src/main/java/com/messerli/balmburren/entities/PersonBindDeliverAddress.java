package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="person_and_deliver_address")
public class PersonBindDeliverAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version @GeneratedValue(strategy = GenerationType.AUTO)
    private long version;
    @ManyToOne
    private User person;
    @ManyToOne
    private Address address;

}
