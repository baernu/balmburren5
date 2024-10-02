package com.messerli.balmburren.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_and_role")
public class UsersRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;
    @Version
    @Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version = 0L;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude // Exclude user to prevent circular reference
//    @EqualsAndHashCode.Exclude
    @ManyToOne
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id")
    @ManyToOne
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
