package com.expensetracker.model;

public class JwtResponse {
    private String token;
    private String email; // Added email field

    public JwtResponse() {}

    public JwtResponse(String token) {
        this.token = token;
    }

    // Constructor to include email
    public JwtResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
