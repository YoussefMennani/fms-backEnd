//package com.fleetmanagementsystem.driverservice.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@RequiredArgsConstructor
//@Configuration
//public class WebSecurityConfig {
//
//    private final JwtAuthConverter jwtAuthConverter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
//                        //.requestMatchers(HttpMethod.GET, "/api/movies", "/api/movies/**").permitAll()
////                        .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
////                        .requestMatchers("/api/movies/*/comments").hasAnyRole(MOVIES_ADMIN, MOVIES_USER)
////                        .requestMatchers("/api/movies", "/api/movies/**").hasRole(MOVIES_ADMIN)
//                        .requestMatchers("/api/userextras/me").hasAnyRole(ROLE_ADMIN, ROLE_USER,ROLE_MANAGER)
//                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
//                        .anyRequest().authenticated())
//                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(
//                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(Customizer.withDefaults())
//                .build();
//    }
//
//    public static final String ROLE_ADMIN = "ADMIN";
//    public static final String ROLE_USER = "USER";
//    public static final String ROLE_MANAGER = "MANAGER";
//}