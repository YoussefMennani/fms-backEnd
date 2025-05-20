package com.fleetManagementSystem.commons.user;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "userextras")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMinimal {

    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
