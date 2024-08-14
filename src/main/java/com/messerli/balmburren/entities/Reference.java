package com.messerli.balmburren.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reference")
public class Reference {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Version
    private Long version;
    private String name;
    private Long value = 0L;
}