package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="person_and_tour")
public class PersonBindTour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version @GeneratedValue(strategy = GenerationType.AUTO)
    private long version;
    @ManyToOne
    private User user;
    @ManyToOne
    private Tour tour;
    @ManyToOne
    private Dates startDate;
    @ManyToOne
    private Dates endDate;
    private Integer position;

}
