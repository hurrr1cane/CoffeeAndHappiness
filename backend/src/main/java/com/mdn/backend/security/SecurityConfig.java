package com.mdn.backend.security;

import com.mdn.backend.model.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Configuration class for setting up security filters and rules.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider provider;
    private final JwtAuthenticationFilter filter;
    private final LogoutHandler logoutHandler;

    /**
     * Configures the security filter chain with rules and filters.
     *
     * @param http The HttpSecurity object to configure.
     * @return A configured SecurityFilterChain.
     * @throws Exception If an error occurs while configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults())
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
                )
                .permitAll()
                .requestMatchers("/api/auth/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/**")
                .hasAnyRole(Role.USER.name(), Role.WAITER.name(), Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/api/**")
                .hasAnyRole(Role.USER.name(), Role.WAITER.name(), Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/api/**")
                .hasAnyRole(Role.USER.name(), Role.WAITER.name(), Role.ADMIN.name())
                .requestMatchers("/api/order/**")
                .hasAnyRole(Role.WAITER.name(), Role.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();

    }

}
