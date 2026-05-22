package com.organization.taskManagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * STEP-BY-STEP INTERNAL WORKINGS:
 * 
 * 1. This filter intercepts every incoming HTTP request (OncePerRequestFilter).
 * 2. It checks for a JWT token in the "Authorization" header.
 * 3. If a valid token is found, it "authenticates" the user by setting the security context.
 * 4. This allows Spring Security to know who is making the request for subsequent steps.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserInfoService userInfoService;

    public JwtAuthFilter(JwtService jwtService, UserInfoService userInfoService) {
        this.jwtService = jwtService;
        this.userInfoService = userInfoService;
    }

    // List of URLs that do not require authentication
    public static final String[] PUBLIC_URLS  = {
            "/auth/register",
            "/generateToken",
            "/auth/login",
            "/error/**"
    };

    /**
     * INTERNAL FLOW: Skip Filtering
     * If the requested URL is in the PUBLIC_URLS list, we don't look for a token.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        for (String url : PUBLIC_URLS) {
            if (url.endsWith("/**") && requestURI.startsWith(url.replace("/**", ""))) {
                return true;
            } else if (url.endsWith("/") && requestURI.startsWith(url)) {
                return true;
            } else if (requestURI.equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * INTERNAL FLOW: Main Filter Logic
     * 1. Extract the "Authorization" header.
     * 2. Validate the format (must start with "Bearer ").
     * 3. Extract the token and the employee ID from it.
     * 4. If ID is valid and user is not already authenticated:
     *    a. Load user details from database.
     *    b. Validate the token against the user details.
     *    c. Create an Authentication object and put it in the SecurityContext.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Allow preflight CORS requests
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String employeeId = null;

        // 1. Check for token presence
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else if (authHeader != null) {
            sendUnauthorizedError(response, "Invalid Authorization header format. Use: Bearer <token>");
            return;
        } else {
            // No token found - let the request pass, Spring Security will block it later if the URL is protected
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract employeeId from token
        try {
            employeeId = jwtService.extractEmpId(token);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            sendUnauthorizedError(response, "Token has expired");
            return;
        } catch (Exception e) {
            sendUnauthorizedError(response, "Invalid token: " + e.getMessage());
            return;
        }

        // 3. Authenticate user if not already authenticated in this session
        if (employeeId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userInfoService.loadUserByUsername(employeeId);

                if (jwtService.validateToken(token, userDetails)) {
                    // Create Authentication token for Spring's internal use
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // IMPORTANT: Setting the context makes the user "logged in" for this request
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (UsernameNotFoundException e) {
                sendUnauthorizedError(response, "User not found");
                return;
            }
        }

        // 4. Continue to the next filter in the chain
        filterChain.doFilter(request, response);
    }

    /**
     * INTERNAL FLOW: Error Handling
     * Sends a custom JSON response when authentication fails.
     */
    private void sendUnauthorizedError(HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String jsonResponse = String.format(
                "{\"error\": \"Unauthorized\", \"message\": \"%s\", \"timestamp\": \"%s\"}",
                message,
                java.time.LocalDateTime.now()
        );

        response.getWriter().write(jsonResponse);
    }
}
