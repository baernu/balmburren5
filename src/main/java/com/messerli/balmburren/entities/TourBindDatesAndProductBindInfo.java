package com.messerli.balmburren.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tour_bind_dates_productbindinfo")
public class TourBindDatesAndProductBindInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long version;
    @ManyToOne
    private Tour tour;
    @ManyToOne
    private Dates dates;
    @ManyToOne
    private ProductBindProductDetails productBindInfos;
}
