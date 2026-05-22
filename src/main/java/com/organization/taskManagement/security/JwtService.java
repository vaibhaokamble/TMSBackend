package com.organization.taskManagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * STEP-BY-STEP INTERNAL WORKINGS:
 * 
 * 1. This service handles all JSON Web Token (JWT) operations: creation, parsing, and validation.
 * 2. It uses the Secret Key defined in application.properties to sign and verify tokens.
 * 3. JWTs consist of Header, Payload (Claims), and Signature.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * INTERNAL FLOW: Token Generation
     * 1. Create a map of 'claims' (data to be stored in the token).
     * 2. Extract user authorities/roles and add them to claims.
     * 3. Use Jwts.builder() to construct the token.
     * 4. Set Subject (employeeId), Issue Date, and Expiration (1 hour).
     * 5. Sign the token using the secret key and HS256 algorithm.
     */
    public String generateToken(String employeeId, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(employeeId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour validity
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * INTERNAL FLOW: Key Management
     * 1. Decodes the Base64 secret key from properties.
     * 2. Returns a cryptographic Key object suitable for HMAC-SHA signing.
     */
    public Key getKey(){
        byte [] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * INTERNAL FLOW: Claim Extraction
     * 1. Parses the entire token to get all claims.
     * 2. Uses a resolver function to pick a specific claim (like Subject or Expiration).
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * INTERNAL FLOW: Token Parsing
     * 1. Uses the Signing Key to verify the token's integrity.
     * 2. If the token was tampered with, parsing will fail here.
     * 3. Returns the payload (Claims) of the token.
     */
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmpId(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * INTERNAL FLOW: Validation
     * 1. Extracts the employee ID from the token.
     * 2. Compares it with the ID from UserDetails.
     * 3. Checks if the current time is before the expiration time in the token.
     */
    public boolean validateToken (String token , UserDetails userDetails){
        final String empId = extractEmpId(token);
        return (empId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

}
