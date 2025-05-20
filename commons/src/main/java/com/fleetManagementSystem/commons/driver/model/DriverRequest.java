package com.fleetManagementSystem.commons.driver.model;

import com.fleetManagementSystem.commons.organization.Organization;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "drivers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DriverRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String organization;

    private String phoneNumber;

    private String address;

    private String cityOfBirth;


    private Long birthDate;


    private String status;

    @Builder.Default
    private Boolean available = true;


    private List<String> language;

    private String profileImageUrl;
    private String documentUrl;


    private List<License> licenses;

    @Builder.Default
    private double rate = 0.0;


}
