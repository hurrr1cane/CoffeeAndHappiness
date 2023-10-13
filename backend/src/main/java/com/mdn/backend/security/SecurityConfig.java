package com.mdn.backend.security;

import com.mdn.backend.model.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Configuration class for setting up security filters and rules.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String URL_FOR_EVERYTHING = "/api/**";
    private final AuthenticationEntryPoint authEntryPoint;
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
                .requestMatchers("/api/order/**")
                .hasAnyRole(Role.WAITER.name(), Role.ADMIN.name())
                .requestMatchers("/api/user/me/**")
                .hasAnyRole(Role.USER.name(), Role.WAITER.name(), Role.ADMIN.name())
                .requestMatchers("/api/review/**")
                .hasAnyRole(Role.USER.name(), Role.WAITER.name(), Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, URL_FOR_EVERYTHING)
                .permitAll()
                .requestMatchers(HttpMethod.POST, URL_FOR_EVERYTHING)
                .hasAnyRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, URL_FOR_EVERYTHING)
                .hasAnyRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, URL_FOR_EVERYTHING)
                .hasAnyRole(Role.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();

    }

}
