package com.fleetManagementSystem.commons.vehicle.dto;

import com.fleetManagementSystem.commons.vehicle.model.RuleGroup;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MinimalVehicle {

    private String id;

    private String licensePlate;

    private List<RuleGroup> ruleGroupList;

}
