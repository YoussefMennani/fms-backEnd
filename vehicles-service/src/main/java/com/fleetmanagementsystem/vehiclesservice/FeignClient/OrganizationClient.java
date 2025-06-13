package com.fleetmanagementsystem.vehiclesservice.FeignClient;

import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.user.UserMinimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

////@FeignClient(name = "user-service", url = "http://localhost:8093/api")
//@FeignClient(name = "user-service", url = "http://geoloc-user-service:8093/api")
//public interface OrganizationClient {
//
//    @GetMapping("/organizations/roots")
//    List<Organization> getRootOrganizations(@RequestHeader("Authorization") String token);
//
//    @GetMapping("/organizations/{idOrganization}")
//    ResponseEntity<Organization> getOrganizationById(@RequestHeader("Authorization") String token,@PathVariable String idOrganization);
//
//    @PostMapping("/userextras/getUsersFromList")
//    public Optional<List<UserMinimal>> getUsersFromList(@RequestHeader("Authorization") String token, @RequestBody List<String> usersList);
//
//
//}

@FeignClient(
        name = "user-service",
        url = "${user-service.base-url}"
)
public interface OrganizationClient {
    @GetMapping("/organizations/roots")
    List<Organization> getRootOrganizations(@RequestHeader("Authorization") String token);

    @GetMapping("/organizations/{idOrganization}")
    ResponseEntity<Organization> getOrganizationById(
            @RequestHeader("Authorization") String token,
            @PathVariable String idOrganization
    );

    @PostMapping("/userextras/getUsersFromList")
    Optional<List<UserMinimal>> getUsersFromList(
            @RequestHeader("Authorization") String token,
            @RequestBody List<String> usersList
    );
}