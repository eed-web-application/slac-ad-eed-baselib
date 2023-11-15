package edu.stanford.slac.ad.eed.baselib.auth.jwt;

import edu.stanford.slac.ad.eed.baselib.auth.test_mock_auth.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.auth.test_mock_auth.config.AppProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    private final AppProperties appProperties;
    private final ApplicationContext applicationContext;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure HttpSecurity with the newer method authorizeHttpRequests
        http
                .csrf(AbstractHttpConfigurer::disable) // Disabling CSRF protection
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session management to stateless
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/**").permitAll() // Permit all requests
                        .anyRequest().authenticated() // All other requests need to be authenticated
                )
                .addFilterBefore(
                        new SLACAuthenticationFilter(
                                "/**",
                                applicationContext.getBean(AuthenticationManager.class),
                                appProperties
                        ),
                        AnonymousAuthenticationFilter.class
                );

        return http.build();
    }


    @Bean
    public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        return new AnonymousAuthenticationFilter("anonymousKey");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}




