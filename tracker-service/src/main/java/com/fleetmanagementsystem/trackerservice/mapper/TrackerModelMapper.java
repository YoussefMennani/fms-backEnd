package com.fleetmanagementsystem.trackerservice.mapper;


import com.fleetManagementSystem.commons.tracker.dto.TrackerBrandResponse;
import com.fleetManagementSystem.commons.tracker.dto.TrackerModelRequest;
import com.fleetManagementSystem.commons.tracker.dto.TrackerModelResponse;
import com.fleetManagementSystem.commons.tracker.model.TrackerBrand;
import com.fleetManagementSystem.commons.tracker.model.TrackerModel;
import com.fleetmanagementsystem.trackerservice.repository.TrackerBrandRepository;
import com.fleetmanagementsystem.trackerservice.service.TrackerBrandService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackerModelMapper {

    @Autowired
    private TrackerBrandRepository trackerBrandRepository;
    @Autowired
    private TrackerBrandMapper trackerBrandMapper;

    @Autowired
    private TrackerBrandService trackerBrandService;

    public TrackerModel toTrackerModel(TrackerModelRequest trackerModelRequest){
        if (trackerModelRequest == null){
            return null;
        }

        TrackerBrand brand = trackerBrandRepository
                .findById(trackerModelRequest.brand())
                .orElseThrow(() -> new NotFoundException("Model not found"));

        //  TrackerBrandResponse trackerBrandResponse = trackerBrandService.findTrackerBrandById(trackerModelRequest.id());

        return TrackerModel.builder()
                .id(trackerModelRequest.id())
                .modelName(trackerModelRequest.modelName())
                .batteryLife(trackerModelRequest.batteryLife())
                .networkType(trackerModelRequest.networkType())
                .features(trackerModelRequest.features())
                .brand(brand)
                .build();
    }


    public TrackerModelResponse toTrackerModelResponse(TrackerModel trackerModel){
        if (trackerModel == null){
            return null;
        }

        //        TrackerBrand brand = trackerBrandRepository
        //                .findById(trackerModel.getBrand().getId())
        //                .orElseThrow(() -> new NotFoundException("Model not found"));
        //        TrackerBrandResponse trackerBrandResponse = trackerBrandService.findTrackerBrandById(trackerModel.getId());

        TrackerBrandResponse trackerBrandResponse = TrackerBrandResponse.builder()
                .id(trackerModel.getBrand().getId())
                .brandName(trackerModel.getBrand().getBrandName())
                .originCountry(trackerModel.getBrand().getOriginCountry())
                .build();

        return new TrackerModelResponse(
                trackerModel.getId(),
                trackerModel.getModelName(),
                trackerModel.getNetworkType(),
                trackerModel.getBatteryLife(),
                trackerModel.getFeatures(),
                trackerBrandResponse
         );
    }

}
