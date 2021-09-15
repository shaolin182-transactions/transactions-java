package org.transactions.api.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SCOPE_READER = "SCOPE_reader";

    private static final String SCOPE_WRITER = "SCOPE_writer";

    private static final String SCOPE_ADMIN = "SCOPE_admin";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.GET).hasAnyAuthority(SCOPE_WRITER, SCOPE_READER, SCOPE_ADMIN)
                        .antMatchers(HttpMethod.POST).hasAnyAuthority(SCOPE_WRITER, SCOPE_ADMIN)
                        .antMatchers(HttpMethod.DELETE).hasAnyAuthority(SCOPE_WRITER, SCOPE_ADMIN)
                        .antMatchers(HttpMethod.PUT).hasAnyAuthority(SCOPE_WRITER, SCOPE_ADMIN)
                        .antMatchers(HttpMethod.PATCH).hasAnyAuthority(SCOPE_WRITER, SCOPE_ADMIN)
                        .anyRequest()
                            .authenticated()
                .and()
                    .oauth2ResourceServer()
                        .jwt();
    }
}
