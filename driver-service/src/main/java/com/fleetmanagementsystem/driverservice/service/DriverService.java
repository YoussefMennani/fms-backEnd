package com.fleetmanagementsystem.driverservice.service;


import com.fleetManagementSystem.commons.driver.model.Driver;
import com.fleetManagementSystem.commons.driver.model.DriverRequest;
import com.fleetManagementSystem.commons.driver.model.DriverResponse;
import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetmanagementsystem.driverservice.FeignClient.OrganizationClient;
import com.fleetmanagementsystem.driverservice.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private OrganizationClient organizationClient;

    public Driver addDriver(DriverRequest driverRequest) {
        // Add any business logic here, e.g., checking for duplicates
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();

        // Fetch the organization details
        Organization organization = organizationClient.getOrganizationById(token, driverRequest.getOrganization()).getBody();

        // Build the Driver object using the DriverRequest data
        Driver driver = toDriver(driverRequest, organization);


        // Save the Driver object to the repository
        return driverRepository.save(driver);
    }

    public List<DriverResponse> getAllDrivers() {
        // Extract the token from the security context
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();

        // Fetch root organizations using the token
        List<Organization> organizationList = organizationClient.getRootOrganizations(token);

        // Extract organization IDs from the organizationList
        Set<String> organizationIds = organizationList.stream()
                .map(Organization::getId)
                .collect(Collectors.toSet());

        // Fetch all drivers
        List<Driver> drivers = driverRepository.findAll();

        // Filter drivers based on organization IDs
        List<Driver> filteredDrivers = drivers.stream()
                .filter(driver -> organizationIds.contains(driver.getOrganization().getId()))
                .toList();

        // Map filtered drivers to DriverResponse
        return filteredDrivers.stream()
                .map(this::toDriverResponse)
                .collect(Collectors.toList());
    }
    public DriverResponse getDriverById(String id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));

        // Map the Driver entity to a DriverResponse object
        return toDriverResponse(driver);
    }

    public Driver updateAvailabilityDriver(String driverId, Boolean available){
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));
        driver.setAvailable(available);
        driverRepository.save(driver);
        return driver;
    }


    public Driver toDriver(DriverRequest driverRequest, Organization organization) {
        return Driver.builder()
                .firstName(driverRequest.getFirstName())
                .lastName(driverRequest.getLastName())
                .email(driverRequest.getEmail())
                .organization(organization)
                .phoneNumber(driverRequest.getPhoneNumber())
                .address(driverRequest.getAddress())
                .cityOfBirth(driverRequest.getCityOfBirth())
                .birthDate(driverRequest.getBirthDate())
                .status(driverRequest.getStatus())
                .available(driverRequest.getAvailable())
                .language(driverRequest.getLanguage())
                .profileImageUrl(driverRequest.getProfileImageUrl())
                .documentUrl(driverRequest.getDocumentUrl())
                .licenses(driverRequest.getLicenses())
                .rate(driverRequest.getRate())
                .build();
    }

    public DriverResponse toDriverResponse(Driver driver) {
        return DriverResponse.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .email(driver.getEmail())
                .organization(driver.getOrganization().getName())
                .phoneNumber(driver.getPhoneNumber())
                .address(driver.getAddress())
                .cityOfBirth(driver.getCityOfBirth())
                .birthDate(driver.getBirthDate())
                .status(driver.getStatus())
                .available(driver.getAvailable())
                .language(driver.getLanguage())
                .profileImageUrl(driver.getProfileImageUrl())
                .documentUrl(driver.getDocumentUrl())
                .licenses(driver.getLicenses())
                .rate(driver.getRate())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();
    }
}
