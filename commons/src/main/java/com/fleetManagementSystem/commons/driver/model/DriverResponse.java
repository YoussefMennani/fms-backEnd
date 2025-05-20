package com.fleetManagementSystem.commons.driver.model;


import com.fleetManagementSystem.commons.organization.Organization;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DriverResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Organization organization;
    private String phoneNumber;
    private String address;
    private String cityOfBirth;
    private Long birthDate;
    private String status;
    private Boolean available;
    private List<String> language;
    private String profileImageUrl;
    private String documentUrl;
    private List<License> licenses;
    private double rate;
    private Date createdAt;
    private Date updatedAt;
}