package com.expensetracker.repository;

import com.expensetracker.model.Expense;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Corrected: Added this method as per service implementation
    List<Expense> findByUserId(Long userId);

    Page<Expense> findByUserId(Long userId, Pageable pageable);

    List<Expense> findByUserIdAndExpenseDateBetween(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e " +
            "WHERE e.userId = :userId AND e.expenseDate BETWEEN :start AND :end GROUP BY e.category")
    List<Object[]> sumAmountByCategory(Long userId, LocalDate start, LocalDate end);
}