package com.expensetracker.service.impl;

import com.expensetracker.model.User;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.UserService; // Ensure this import is correct if UserService is an interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        // Ensure the password is encoded before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set a default role, if not already set by the registration request
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER"); // Default role
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        // This method might be used for updates where password encoding is not always needed,
        // or for admin-like operations. Be careful with direct saveUser if you expect
        // password to be raw. If used for new users/password changes, encode here too.
        // For simplicity, assuming registerUser handles new user encoding.
        return userRepository.save(user);
    }
}