package com.fleetmanagementsystem.vehiclenotificationservice.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize ->
                        authorize.requestMatchers(
                                        "/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "http://localhost:5173/**",
                                        "/ws/**"  // Include WebSocket endpoint in CORS rules
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .formLogin().disable()         // Disable the default form login
                .csrf().disable()             // Disable CSRF if needed
                .cors(); // Enable CORS support globally

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow CORS for your frontend origin
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // Your React frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
