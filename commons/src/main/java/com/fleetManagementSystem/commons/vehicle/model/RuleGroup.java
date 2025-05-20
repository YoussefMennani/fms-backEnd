package com.fleetManagementSystem.commons.vehicle.model;

import com.fleetManagementSystem.commons.user.UserMinimal;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "ruleGroups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RuleGroup {
    @Id
    private String id;
    private String ruleGroupName;
    private String username;

    private List<Rule> rules;

    @DBRef(lazy = true)
    private List<UserMinimal> users;
    // Getters and Setters

}
