package com.mdn.backend.security;

import com.mdn.backend.exception.UserNotFoundException;
import com.mdn.backend.repository.UserRepository;
import com.mdn.backend.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom authentication filter that processes JWT tokens.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    /**
     * Filters incoming requests to authenticate using JWT tokens.
     *
     * @param request     The incoming HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If a servlet exception occurs.
     * @throws IOException      If an I/O exception occurs.
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        email = jwtService.extractEmail(jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            var user = userRepository.findByEmail(email).orElseThrow(
                    () -> new UserNotFoundException("User not found with email: " + email)
            );
            if (user.isEnabled()) {
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
            filterChain.doFilter(request, response);
        }

    }
}
