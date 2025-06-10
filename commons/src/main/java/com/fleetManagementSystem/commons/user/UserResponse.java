package com.fleetManagementSystem.commons.user;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private String profileName;
    private String profileRole;
    private String organizationName;
    private String phoneNumber;
}
