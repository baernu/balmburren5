package com.messerli.balmburren.entities;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.util.Date;

@Table(name = "roles")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;
    @Version @GeneratedValue(strategy = GenerationType.AUTO)
    private long version;

//    @Getter
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public void setName(RoleEnum roleName) {
        this.name = roleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleEnum getName() { return this.name;}


    // Getters and setters here....
}
