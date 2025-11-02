package com.expensetracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Getter @Setter
public class ExpenseDTO {
    private Long id;

    @Size(max = 500)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal amount; // Corrected: ensure this is BigDecimal

    @NotBlank
    private String category;

    @NotNull
    private LocalDate expenseDate;

    private boolean recurring;

    // lombok handles getters & setters
}