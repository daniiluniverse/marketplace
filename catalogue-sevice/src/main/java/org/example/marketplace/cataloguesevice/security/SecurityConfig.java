package org.example.marketplace.cataloguesevice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

       return http
               .authorizeHttpRequests(auth -> auth.requestMatchers( HttpMethod.POST,
                       "/app/products/new")
                       .hasAuthority("SCOPE_edit_catalogue")
                       .requestMatchers(HttpMethod.PUT, "/app/products/{id:\\d+}")
                       .hasAuthority("SCOPE_edit_catalogue")
                       .requestMatchers(HttpMethod.DELETE, "/app/products/{id:\\d+}")
                       .hasAuthority("SCOPE_edit_catalogue")
                       .requestMatchers(HttpMethod.GET)
                       .permitAll()
                     //  .hasAuthority("SCOPE_view_catalogue")
                       .anyRequest()
                       .denyAll())
               .csrf(CsrfConfigurer::disable)
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()))
                .build();
    }
}
