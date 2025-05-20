package com.fleetmanagementsystem.userservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String organization;
    private String profile;
    private String phoneNumber;
    private String address;
    private String cityOfBirth;
    private String status;
    private List<String> language;
    private String profileImageUrl;
    private String documentUrl;
    private String username;
    private String password;
    private Long birthDate;
}
