package com.fleetmanagementsystem.userservice.FeignClient;

import com.fleetManagementSystem.commons.driver.model.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "subscription-service", url = "http://localhost:8089/api/v1/subscription")
public interface SubscriptionClient {

    @GetMapping("/isOrganizationAuthorized")
    boolean isOrganizationAuthorized(@RequestHeader("Authorization") String token,@RequestParam String organizationId);

}