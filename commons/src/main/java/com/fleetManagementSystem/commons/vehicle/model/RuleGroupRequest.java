package com.fleetManagementSystem.commons.vehicle.model;

import com.fleetManagementSystem.commons.user.UserMinimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "ruleGroups")
public class RuleGroupRequest {
    @Id
    private String id;
    private String ruleGroupName;
    private String username;

    private List<Rule> rules;

    private List<String> users;
    // Getters and Setters

}
