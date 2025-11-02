package com.expensetracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String description; // instead of title

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String category;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(nullable = false)
    private boolean recurring;
}
