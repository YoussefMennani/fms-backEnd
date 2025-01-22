package com.fleetManagementSystem.commons.tracker.model;


import com.fleetManagementSystem.commons.enums.GPSStatus;
import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.tracker.dto.TrackerRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "trackers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tracker {

    @Id
    private String id;

    private String imei;

    private GPSStatus status;

    @DBRef(lazy = true)
    private Organization organization;

    @Builder.Default
    private boolean isVehicleAssociated = false;
    private String simSerialNumber;

    private String simNumber;

    @Builder.Default
    private boolean isMoving = false;
    @DBRef(lazy = true)
    private TrackerModel model;

    @DBRef(lazy = true)
    private TrackerBrand brand;

    private LocalDateTime last_connection;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public boolean isEquivalentTo(TrackerRequest request) {
        if (request == null) {
            return false;
        }

        // Check each field for equality
        boolean idEquals = (id == null && request.id() == null) || (id != null && id.equals(request.id()));
        boolean imeiEquals = imei.equals(request.imei());
        boolean simSerialNumberEquals = simSerialNumber.equals(request.simSerialNumber());
        boolean simNumberEquals = simNumber.equals(request.simNumber());
        boolean modelEquals = model.equals(request.model()); // Direct list comparison
        boolean brandEquals = brand.equals(request.brand()); // Assuming `TrackerBrand` has a `getName()` method
        boolean statusEquals = status.equals(request.status());
        // Return true only if all fields match
        return idEquals && imeiEquals && simSerialNumberEquals && simNumberEquals && modelEquals && brandEquals && statusEquals;
    }



}
