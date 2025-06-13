package com.fleetmanagementsystem.gatewayservice.security;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {

        //        return NimbusReactiveJwtDecoder.withJwkSetUri("https://45.10.162.39:8443/realms/fleet-management-system/protocol/openid-connect/certs").build();
        // Create insecure (trust-all) SSL context for Reactor Netty
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create().secure(spec -> spec.sslContext(sslContext));

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return NimbusReactiveJwtDecoder.withJwkSetUri("https://45.10.162.39:8443/realms/fleet-management-system/protocol/openid-connect/certs")
                .webClient(webClient)
                .build();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/eureka/**", "/actuator/health", "/actuator/health/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();

        http.cors().configurationSource(corsConfigurationSource());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }



    @Bean
    public WebFilter logRequestsFilter() {
        Logger logger = LoggerFactory.getLogger("RequestLogger");
        return (exchange, chain) -> {
            logger.info("Incoming request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

            return ReactiveSecurityContextHolder.getContext()
                    .map(securityContext -> securityContext.getAuthentication())
                    .doOnNext(auth -> {
                        if (auth != null) {
                            logger.info("Authentication: {} , Authorities: {}", auth.getName(), auth.getAuthorities());
                        } else {
                            logger.info("No Authentication present");
                        }
                    })
                    .then(chain.filter(exchange));
        };
    }



}
