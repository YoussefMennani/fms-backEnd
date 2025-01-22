package com.fleetManagementSystem.commons.tracker.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
@Builder
public class TrackerBrandResponse {

    private String id;
    private String originCountry;
    private String brandName;

}
