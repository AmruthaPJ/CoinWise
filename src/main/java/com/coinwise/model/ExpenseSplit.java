package com.coinwise.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "expense_splits")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseSplit {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "amount_owed", nullable = false)
    private Double amountOwed;

    @Column(name = "percent")
    private Double percent;

    @Builder.Default
    @Column(name = "is_paid")
    private Boolean isPaid = false;
}
