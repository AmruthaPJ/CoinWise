package com.coinwise.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "budgets")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Budget {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "limit_amount", nullable = false)
    private Double limitAmount;

    @Builder.Default
    @Column(name = "current_spent")
    private Double currentSpent = 0.0;

    @Builder.Default
    @Column(name = "is_exceeded")
    private Boolean isExceeded = false;
}
