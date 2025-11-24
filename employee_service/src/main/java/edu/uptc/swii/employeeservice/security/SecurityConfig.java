package edu.uptc.swii.employeeservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        PasetoService pasetoService = new PasetoService();
        PasetoAuthenticationFilter pasetoFilter = new PasetoAuthenticationFilter(pasetoService);

        http.csrf(csrf -> csrf.disable())
                .addFilterBefore(pasetoFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Permite acceso libre al endpoint de búsqueda por documento
                        .requestMatchers("/employee/findbydocument/**").permitAll()
                        // Protege el resto para ADMIN
                        .requestMatchers("/employee/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
        return http.build();
    }
}
