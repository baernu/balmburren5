package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="dates")
public class Dates {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    @Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
    private long version = 0L;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss a z")
    private String date;
}
