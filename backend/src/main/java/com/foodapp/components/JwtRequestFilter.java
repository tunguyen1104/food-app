package com.foodapp.components;

import com.foodapp.constants.SecurityConstants;
import com.foodapp.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Check if the request belonging to any whitelist endpoint
        if (isBypassRequest(request)) {
            filterChain.doFilter(request, response); // enable bypass
            return;
        }
        // Check if the request has token in header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Auth header is null");
            return;
        }
        // Check if the token valid
        final String token = authHeader.substring(7);
        String phone;
        try {
            phone = jwtTokenUtil.extractPhone(token);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        if (phone != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) userDetailsService.loadUserByUsername(phone);
            if (!jwtTokenUtil.validateToken(token, user)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            }
            var authentication = new UsernamePasswordAuthenticationToken(phone, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response); // Enable bypass
    }

    private boolean isBypassRequest(@NonNull HttpServletRequest request) {
        var requestPath = request.getRequestURI();
        return Arrays.stream(SecurityConstants.BYPASS_ENDPOINTS).anyMatch(requestPath::matches);
    }
}
