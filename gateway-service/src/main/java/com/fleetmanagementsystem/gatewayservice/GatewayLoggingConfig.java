package com.fleetmanagementsystem.gatewayservice;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class GatewayLoggingConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayLoggingConfig.class);

    @Bean
    public GlobalFilter logFilter() {
        return new GlobalFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                String method =exchange.getRequest().getMethod().name();
                String originalUri = exchange.getRequest().getURI().toString();
                logger.info("Incoming request: {} {}", method, originalUri);

                // Get the matched Route from exchange attributes
                Route route = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute");
                if (route != null) {
                    String routeId = route.getId();
                    String routeUri = route.getUri().toString();
                    logger.info("Routing request to routeId: {}, URI: {}", routeId, routeUri);
                } else {
                    logger.warn("No route matched for request {}", originalUri);
                }

                return chain.filter(exchange);
            }
        };
    }
}
