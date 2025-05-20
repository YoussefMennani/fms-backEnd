package com.fleetManagementSystem.commons.tracker.model;


import com.fleetManagementSystem.commons.tracker.dto.TrackerBrandRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "tracker_brands")
@Data  // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor  // Generates a no-arguments constructor
@AllArgsConstructor // Generates an all-arguments constructor
@Builder
public class TrackerBrand {

    @Id
    private String id;

    private String originCountry;
    private String brandName;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public boolean isEquivalentTo(TrackerBrandRequest request) {
        if (request == null) {
            return false;
        }

        // Check each field for equality
        boolean idEquals = (id == null && request.id() == null) || (id != null && id.equals(request.id()));
        boolean originCountryEquals = originCountry.equals(request.originCountry());
        boolean brandNameEquals = brandName.equals(request.brandName());

        return idEquals && originCountryEquals && brandNameEquals;

    }


}
