package com.fleetManagementSystem.commons.tracker.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MinimalTrackerResponse {
    private String imei;
    private String trackerId;
}
