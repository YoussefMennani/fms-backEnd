package com.fleetmanagementsystem.trackerservice.FeignClient;

import com.fleetManagementSystem.commons.organization.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

//@FeignClient(name = "user-service", url = "http://localhost:8093/api/organizations")
@FeignClient(
        name = "user-service",
        url = "${user-service.base-url}"
)
public interface OrganizationClient {

    @GetMapping("/roots")
    List<Organization> getRootOrganizations(@RequestHeader("Authorization") String token);


    @GetMapping("/{idOrganization}")
    ResponseEntity<Organization> getOrganizationById(@RequestHeader("Authorization") String token,@PathVariable String idOrganization);


}