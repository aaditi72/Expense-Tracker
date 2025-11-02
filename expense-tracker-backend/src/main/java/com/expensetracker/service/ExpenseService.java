package com.expensetracker.service;

import com.expensetracker.model.Expense;
import java.util.List;

public interface ExpenseService {

    List<Expense> getAllExpensesByEmail(String userEmail);
    Expense getExpenseByIdForUser(Long id, String userEmail);
    Expense addExpenseForUser(Expense expense, String userEmail);
    Expense updateExpenseForUser(Long id, Expense expense, String userEmail);
    void deleteExpenseForUser(Long id, String userEmail);
}