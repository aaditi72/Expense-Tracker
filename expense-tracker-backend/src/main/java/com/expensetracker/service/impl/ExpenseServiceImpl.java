package com.expensetracker.service;

import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.exception.ResourceNotFoundException; // Corrected: Import custom exceptions
import com.expensetracker.exception.UnauthorizedAccessException; // Corrected: Import custom exceptions
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // Helper to get User by email (now private to service impl)
    private User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));
    }

    // Helper to check if expense belongs to user (now private to service impl)
    private void checkExpenseOwnership(Expense expense, Long userId) {
        if (!Objects.equals(expense.getUserId(), userId)) {
            throw new UnauthorizedAccessException("Expense not found or does not belong to user.");
        }
    }

    @Override
    public List<Expense> getAllExpensesByEmail(String userEmail) {
        User user = getUserByEmail(userEmail);
        return expenseRepository.findByUserId(user.getId());
    }

    @Override
    public Expense getExpenseByIdForUser(Long id, String userEmail) {
        User user = getUserByEmail(userEmail);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        checkExpenseOwnership(expense, user.getId());
        return expense;
    }

    @Override
    @Transactional
    public Expense addExpenseForUser(Expense expense, String userEmail) {
        User user = getUserByEmail(userEmail);
        expense.setUserId(user.getId());
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public Expense updateExpenseForUser(Long id, Expense expenseDetails, String userEmail) {
        User user = getUserByEmail(userEmail);
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        checkExpenseOwnership(existingExpense, user.getId());

        existingExpense.setDescription(expenseDetails.getDescription());
        existingExpense.setAmount(expenseDetails.getAmount());
        existingExpense.setCategory(expenseDetails.getCategory());
        existingExpense.setExpenseDate(expenseDetails.getExpenseDate());
        existingExpense.setRecurring(expenseDetails.isRecurring());

        return expenseRepository.save(existingExpense);
    }

    @Override
    @Transactional
    public void deleteExpenseForUser(Long id, String userEmail) {
        User user = getUserByEmail(userEmail);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        checkExpenseOwnership(expense, user.getId());
        expenseRepository.delete(expense);
    }
}