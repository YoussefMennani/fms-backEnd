package com.fleetManagementSystem.commons.driver.dto;

import lombok.*;

import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MinimalDriver {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String organization;

    private String phoneNumber;

}
