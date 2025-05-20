package com.fleetManagementSystem.commons.vehicle.dto;

import com.fleetManagementSystem.commons.vehicle.model.RuleGroup;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignVLRulesRequest {
     private String vehicleId;
     private List<String> ruleGroupIds;
}