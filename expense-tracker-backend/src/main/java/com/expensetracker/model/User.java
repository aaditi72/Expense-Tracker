package com.expensetracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CRITICAL FIX: Ensure email is NOT NULL and UNIQUE in the DB
    @Column(nullable = false, unique = true)
    private String email;

    // CRITICAL FIX: The password field in the entity.
    // We want the DB column to be 'password', and it must be NOT NULL.
    // Hibernate will map 'password' field to 'password' column by default.
    @Column(nullable = false) // This will ensure the DB column 'password' is NOT NULL
    private String password;

    // This field already has a default in the entity, so we ensure it's not nullable in DB
    @Column(nullable = false) // This will ensure the DB column 'role' is NOT NULL
    private String role = "USER";

    // IMPORTANT: Remove any fields that are not in your entity
    // e.g., 'created_at', 'name' (if you don't have them here)
    // If you want 'name', add it:
    // private String name;
}