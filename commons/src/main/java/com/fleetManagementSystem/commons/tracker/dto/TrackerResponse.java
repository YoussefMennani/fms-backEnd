package com.fleetManagementSystem.commons.tracker.dto;

import com.fleetManagementSystem.commons.enums.GPSStatus;
import com.fleetManagementSystem.commons.organization.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
public class TrackerResponse {

    private String id;
    private String imei;
    private GPSStatus status;
    private TrackerModelResponse model;
    //    private TrackerBrandResponse brand;
    private String simSerialNumber;
    private String simNumber;
    private boolean isVehicleAssociated;
    private boolean isMoving;
    private Organization organization;

}
