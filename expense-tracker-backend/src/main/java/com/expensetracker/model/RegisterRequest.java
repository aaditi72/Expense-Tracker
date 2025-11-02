package com.expensetracker.model;

// Consider adding Lombok's @Getter and @Setter here for cleaner code,
// but for maximum clarity on the fix, I'll put explicit getters/setters.
// If you have Lombok set up and prefer it, you can remove explicit
// getters/setters and just add @Getter @Setter.

public class RegisterRequest {
    private String name;
    private String email;
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() { // <--- THIS IS THE CRITICAL MISSING METHOD
        return email;
    }

    public void setEmail(String email) { // <--- And its setter
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}