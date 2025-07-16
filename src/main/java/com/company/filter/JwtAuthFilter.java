package com.company.filter;

import com.company.constants.ErrorConstants;
import com.company.entity.user.JwtUserDetails;
import com.company.entity.user.User;
import com.company.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final List<String> excludedPaths = List.of(
            "/auth/login", "/auth/signup-otp", "/auth/register",
            "/swagger-ui", "/swagger-ui/", "/swagger-ui/index.html", "/swagger-ui.html", "/v3/api-docs", "/v3/api-docs/", "/swagger-resources"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return excludedPaths.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendJsonError(response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject(); // e.g. email or name
            Integer userId = claims.get("userId", Integer.class);
            String role = claims.get("role", String.class);

            if (userId == null || role == null || username == null) {
                logger.error("Missing required claims in JWT");
                sendJsonError(response);
                return;
            }

            // Optional DB check (skip if already validated during login)
            User user = userService.getUser(userId);
            if (user == null) {
                logger.error("User not found: {}", userId);
                sendJsonError(response);
                return;
            }

            // Create custom principal
            JwtUserDetails jwtUserDetails = new JwtUserDetails(userId, role, username);

            // Set authentication in SecurityContext
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            jwtUserDetails,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            logger.error("JWT validation error: {}", e.getMessage());
            sendJsonError(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendJsonError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(ErrorConstants.TOKEN_IS_REQUIRED));
    }
}
