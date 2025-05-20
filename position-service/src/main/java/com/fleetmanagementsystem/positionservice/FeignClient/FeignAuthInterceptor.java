package com.fleetmanagementsystem.positionservice.FeignClient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public void apply(RequestTemplate template) {
        // Fetch the JWT token
        String jwtToken = jwtTokenService.getJwtToken();

        // Add the token to the Authorization header
        template.header("Authorization", "Bearer " + jwtToken);
    }
}
