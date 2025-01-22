package com.fleetManagementSystem.commons.tracker.model;


import com.fleetManagementSystem.commons.tracker.dto.TrackerModelRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "tracker_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackerModel {

    @Id
    private String id;

    private String modelName;
    private List<String> features;
    private String batteryLife;
    private String networkType;

    @DBRef(lazy = false) // Eagerly fetch the Brand data
    private TrackerBrand brand;


    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public boolean isEquivalentTo(TrackerModelRequest request) {
        if (request == null) {
            return false;
        }

        // Check each field for equality
        boolean idEquals = (id == null && request.id() == null) || (id != null && id.equals(request.id()));
        boolean modelNameEquals = modelName.equals(request.modelName());
        boolean networkTypeEquals = networkType.equals(request.networkType());
        boolean batteryLifeEquals = batteryLife.equals(request.batteryLife());
        boolean featuresEquals = features.equals(request.features()); // Direct list comparison
        boolean brandEquals = brand.equals(request.brand()); // Assuming `TrackerBrand` has a `getName()` method

        // Return true only if all fields match
        return idEquals && modelNameEquals && networkTypeEquals && batteryLifeEquals && featuresEquals && brandEquals;
    }



}
