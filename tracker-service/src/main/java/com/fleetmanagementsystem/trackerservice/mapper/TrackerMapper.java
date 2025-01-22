package com.fleetmanagementsystem.trackerservice.mapper;


import com.fleetManagementSystem.commons.tracker.dto.TrackerBrandResponse;
import com.fleetManagementSystem.commons.tracker.dto.TrackerModelResponse;
import com.fleetManagementSystem.commons.tracker.dto.TrackerRequest;
import com.fleetManagementSystem.commons.tracker.dto.TrackerResponse;
import com.fleetManagementSystem.commons.tracker.model.TrackerBrand;
import com.fleetManagementSystem.commons.tracker.model.TrackerModel;
import com.fleetManagementSystem.commons.tracker.model.Tracker;
import com.fleetmanagementsystem.trackerservice.repository.TrackerBrandRepository;
import com.fleetmanagementsystem.trackerservice.repository.TrackerModelRepository;
import com.fleetmanagementsystem.trackerservice.service.TrackerBrandService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackerMapper {

    @Autowired
    private TrackerBrandRepository trackerBrandRepository;

    @Autowired
    private TrackerModelRepository trackerModelRepository;

    @Autowired
    private TrackerBrandMapper trackerBrandMapper;

    @Autowired
    private TrackerBrandService trackerBrandService;

    public Tracker toTracker(TrackerRequest trackerRequest){
        if (trackerRequest == null){
            return null;
        }

        TrackerBrand brand = trackerBrandRepository
                .findById(trackerRequest.brand())
                .orElseThrow(() -> new NotFoundException("brand not found"));


        TrackerModel model = trackerModelRepository
                .findById(trackerRequest.model())
                .orElseThrow(() -> new NotFoundException("model not found"));

        //  TrackerBrandResponse trackerBrandResponse = trackerBrandService.findTrackerBrandById(trackerModelRequest.id());

        return Tracker.builder()
                .id(trackerRequest.id())
                .imei(trackerRequest.imei())
                .status(trackerRequest.status())
                .simSerialNumber(trackerRequest.simSerialNumber())
                .simNumber(trackerRequest.simNumber())
                .model(model)
                .brand(brand)
                .build();
    }


    public TrackerResponse toTrackerResponse(Tracker tracker){
        if (tracker == null){
            return null;
        }

        //        TrackerBrand brand = trackerBrandRepository
        //                .findById(trackerModel.getBrand().getId())
        //                .orElseThrow(() -> new NotFoundException("Model not found"));
        //        TrackerBrandResponse trackerBrandResponse = trackerBrandService.findTrackerBrandById(trackerModel.getId());

        TrackerBrandResponse trackerBrandResponse = TrackerBrandResponse.builder()
                .id(tracker.getBrand().getId())
                .brandName(tracker.getBrand().getBrandName())
                .originCountry(tracker.getBrand().getOriginCountry())
                .build();

        TrackerModelResponse trackerModelResponse = TrackerModelResponse.builder()
                .id(tracker.getModel().getId())
                .modelName(tracker.getModel().getModelName())
                .networkType(tracker.getModel().getNetworkType())
                .batteryLife(tracker.getModel().getBatteryLife())
                .features(tracker.getModel().getFeatures())
                .brand(trackerBrandResponse)
                .build();


        return new TrackerResponse(
                tracker.getId(),
                tracker.getImei(),
                tracker.getStatus(),
                trackerModelResponse,
//                trackerBrandResponse,
                tracker.getSimSerialNumber(),
                tracker.getSimNumber(),
                tracker.isVehicleAssociated(),
                tracker.isMoving(),
                tracker.getOrganization()
         );
    }

}
