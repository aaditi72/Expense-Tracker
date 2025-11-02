package com.expensetracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders; // Import for Base64 decoding
import io.jsonwebtoken.security.Keys; // Import for creating a secure key
import org.springframework.beans.factory.annotation.Value; // Import for @Value
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key; // Import for the Key interface
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Inject the secret key from application.properties
    @Value("${jwt.secret}")
    private String SECRET; // Renamed to avoid confusion with the hardcoded one

    // Inject the expiration time from application.properties
    @Value("${jwt.expirationMs}")
    private long jwtExpiration; // Renamed for clarity

    // Method to get the signing key.
    // It's best practice to use a robust key generation method from jjwt.
    // If your 'jwt.secret' in application.properties is NOT Base64 encoded, use getBytes()
    // If your 'jwt.secret' in application.properties IS Base64 encoded, use Decoders.BASE64.decode(SECRET)
    private Key getSignInKey() {
        // For HS256, it's safer to use Keys.hmacShaKeyFor directly,
        // and ensure the secret string is long enough.
        // Let's assume 'jwt.secret' in application.properties is a plain string
        // that you want to be treated as bytes. jjwt will then generate a secure key from it.
        // It's often recommended to Base64 encode the secret itself.
        // Let's modify it to expect a Base64-encoded secret for robustness.
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ Extract username (email) from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // Use Jwts.parserBuilder() for newer jjwt versions
                .setSigningKey(getSignInKey()) // Use the injected and securely generated key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Generate token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Use injected expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Use the injected and securely generated key
                .compact();
    }

    // ✅ Validate token using UserDetails
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // ✅ If your JwtAuthenticationFilter expects validateToken(token, String)
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}