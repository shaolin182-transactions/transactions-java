package org.transactions.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("!dev")
@EnableWebSecurity
public class SecurityConfig {

    private static final String SCOPE_READER = "SCOPE_reader";

    private static final String SCOPE_WRITER = "SCOPE_writer";

    private static final String SCOPE_ADMIN = "SCOPE_admin";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers(HttpMethod.GET).permitAll()
                    .requestMatchers(HttpMethod.POST).permitAll()
                    .requestMatchers(HttpMethod.DELETE).permitAll()
                    .requestMatchers(HttpMethod.PUT).permitAll()
                    .requestMatchers(HttpMethod.PATCH).permitAll()
                    .anyRequest().authenticated()
            ).oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
