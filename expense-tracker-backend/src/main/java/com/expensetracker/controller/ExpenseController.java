package com.expensetracker.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.expensetracker.dto.ExpenseDTO;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Helper to get current authenticated user's email
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new IllegalStateException("User not authenticated"); // Should not happen with proper security config
    }

    // You'll need a way to get the userId from the email. Let's add this to UserService.
    // For now, let's assume `expenseService` can handle mapping email to userId or
    // we pass the email and the service resolves the userId.
    // For simplicity, let's update ExpenseService to accept email or userId.

    // ✅ GET - All expenses for the current user
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpensesForUser() {
        String userEmail = getCurrentUserEmail();
        return new ResponseEntity<>(expenseService.getAllExpensesByEmail(userEmail), HttpStatus.OK);
    }

    // ✅ GET - Single expense by ID for the current user
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseByIdForUser(@PathVariable Long id) {
        String userEmail = getCurrentUserEmail();
        Expense expense = expenseService.getExpenseByIdForUser(id, userEmail);
        return new ResponseEntity<>(expense, HttpStatus.OK); // Service should handle not found or not owned
    }

    // ✅ POST - Add expense for the current user
    @PostMapping
    public ResponseEntity<Expense> addExpenseForUser(@RequestBody Expense expense) {
        String userEmail = getCurrentUserEmail();
        Expense newExpense = expenseService.addExpenseForUser(expense, userEmail);
        return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
    }

    // ✅ PUT - Update expense for the current user
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpenseForUser(@PathVariable Long id, @RequestBody Expense expense) {
        String userEmail = getCurrentUserEmail();
        Expense updatedExpense = expenseService.updateExpenseForUser(id, expense, userEmail);
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    // ✅ DELETE - Delete expense for the current user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpenseForUser(@PathVariable Long id) {
        String userEmail = getCurrentUserEmail();
        expenseService.deleteExpenseForUser(id, userEmail);
        return new ResponseEntity<>("Expense deleted successfully!", HttpStatus.OK);
    }
}