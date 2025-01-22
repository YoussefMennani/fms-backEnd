package com.fleetmanagementsystem.trackerservice.mapper;


import com.fleetManagementSystem.commons.tracker.dto.TrackerBrandRequest;
import com.fleetManagementSystem.commons.tracker.dto.TrackerBrandResponse;
import com.fleetManagementSystem.commons.tracker.model.TrackerBrand;
import org.springframework.stereotype.Component;

@Component
public class TrackerBrandMapper {

    public TrackerBrand toTrackerBrand(TrackerBrandRequest trackerBrandRequest){
        if (trackerBrandRequest == null){
            return null;
        }
        return TrackerBrand.builder()
                .id(trackerBrandRequest.id())
                .brandName(trackerBrandRequest.brandName())
                .originCountry(trackerBrandRequest.originCountry())
                .build();
    }


    public TrackerBrandResponse fromTrackerBrand(TrackerBrand trackerBrand){
        if (trackerBrand == null){
            return null;
        }
        return new TrackerBrandResponse(
                trackerBrand.getId(),
                trackerBrand.getOriginCountry(),
                trackerBrand.getBrandName()
         );
    }

}
