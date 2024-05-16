package org.transactions.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.transactions.api.config.CorsConfigProperties;

@Configuration
@Profile("!dev")
@EnableWebSecurity
public class SecurityConfig {

    private static final String SCOPE_READER = "SCOPE_reader";

    private static final String SCOPE_WRITER = "SCOPE_writer";

    private static final String SCOPE_ADMIN = "SCOPE_admin";

    @Autowired
    CorsConfigProperties corsConfigProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.GET).hasAnyAuthority(SCOPE_ADMIN, SCOPE_READER, SCOPE_WRITER)
                    .requestMatchers(HttpMethod.POST).hasAnyAuthority(SCOPE_ADMIN, SCOPE_READER, SCOPE_WRITER)
                    .requestMatchers(HttpMethod.DELETE).hasAnyAuthority(SCOPE_ADMIN, SCOPE_READER, SCOPE_WRITER)
                    .requestMatchers(HttpMethod.PUT).hasAnyAuthority(SCOPE_ADMIN, SCOPE_READER, SCOPE_WRITER)
                    .requestMatchers(HttpMethod.PATCH).hasAnyAuthority(SCOPE_ADMIN, SCOPE_READER, SCOPE_WRITER)
                    .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                    .anyRequest().authenticated()
            ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(corsConfigProperties.getAllowedOrigins());
        corsConfiguration.setAllowedHeaders(corsConfigProperties.getAllowedHeaders());
        corsConfiguration.setAllowedMethods(corsConfigProperties.getAllowedMethods());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
