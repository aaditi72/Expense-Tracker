package com.expensetracker.controller;

import com.expensetracker.model.LoginRequest;
import com.expensetracker.model.RegisterRequest;
import com.expensetracker.model.JwtResponse;
import com.expensetracker.security.JwtService;
import com.expensetracker.model.User;
import com.expensetracker.service.UserService; // Corrected: Use UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService; // Corrected: Inject UserService

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // UserService will encode it

        userService.registerUser(user);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        // Corrected: Return email along with token in JwtResponse
        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
    }
}