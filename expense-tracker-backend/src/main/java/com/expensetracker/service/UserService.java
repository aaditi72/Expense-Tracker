package com.expensetracker.service;

import com.expensetracker.model.User;
import java.util.Optional;

public interface UserService {
    User registerUser(User user); // Corrected: Name consistent with AuthController
    Optional<User> findByEmail(String email); // Corrected: Added for checking existing email
    User saveUser(User user);
}