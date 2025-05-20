package com.fleetManagementSystem.commons.tracker.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
@Builder
public class TrackerModelResponse{

    private String id;
    private String modelName;
    private String networkType;
    private String batteryLife;
    private List<String> features;
    private TrackerBrandResponse brand;

}
