package com.sjsu.cmpe272.prodwell.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private void validateToken(String token) throws Exception {
        try {
            if (!isTokenValid(token)) {
                throw new Exception("Invalid token");
            }
        } catch (Exception e) {
            throw new Exception("Token validation failed: " + e.getMessage());
        }
    }

    private boolean isTokenValid(String token) {
        try {
            // Split the token into its parts
            String[] parts = token.split("\\.");
            
            if (parts.length != 3) {
                return false;
            }

            // Decode and verify payload
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode claims = objectMapper.readTree(payload);

            // Only check for oid claim
            return claims.has("oid");
            
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        // Skip authentication for OPTIONS requests
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                validateToken(token);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}