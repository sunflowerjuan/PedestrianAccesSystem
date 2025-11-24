package edu.uptc.swii.loginservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PasetoService pasetoService;

    public SecurityConfig(PasetoService pasetoService) {
        this.pasetoService = pasetoService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        PasetoAuthenticationFilter pasetoFilter = new PasetoAuthenticationFilter(pasetoService);

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admins/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(pasetoFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
