package com.fleetmanagementsystem.trackerservice.service;

import com.fleetManagementSystem.commons.tracker.dto.TrackerModelRequest;
import com.fleetManagementSystem.commons.tracker.dto.TrackerModelResponse;
import com.fleetmanagementsystem.trackerservice.exception.TrackerEntityNotFoundException;
import com.fleetmanagementsystem.trackerservice.mapper.TrackerModelMapper;
import com.fleetManagementSystem.commons.tracker.model.TrackerBrand;
import com.fleetManagementSystem.commons.tracker.model.TrackerModel;
import com.fleetmanagementsystem.trackerservice.repository.TrackerBrandRepository;
import com.fleetmanagementsystem.trackerservice.repository.TrackerModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TrackerModelService {

    private final TrackerModelRepository trackerModelRepository;
    private final TrackerModelMapper trackerModelMapper;

    private final TrackerBrandRepository trackerBrandRepository;

    public List<TrackerModelResponse> getAllTrackerModel() {
        return this.trackerModelRepository
                .findAll()
                .stream()
                .map(this.trackerModelMapper::toTrackerModelResponse)
                .collect(Collectors.toList());
    }


    public TrackerModelResponse createTrackerModel(TrackerModelRequest trackerBrandRequest) {
        var trackerModel = this.trackerModelRepository.save(trackerModelMapper.toTrackerModel(trackerBrandRequest));
        TrackerModelResponse response = this.trackerModelMapper.toTrackerModelResponse(trackerModel);
        return response;
    }

    public TrackerModelResponse findTrackerModelById(String modelId) {
        return  this.trackerModelRepository
                .findById(modelId)
                .map(this.trackerModelMapper::toTrackerModelResponse)
                .orElseThrow(()-> new TrackerEntityNotFoundException(String.format(
                        "No Model found with the provided ID: %s", modelId
                )));
    }

    public TrackerModelResponse putTrackerModelById(String modelId, TrackerModelRequest newTrackerModel) {

        TrackerModel trackerModel =  this.trackerModelRepository.findById(modelId)
                .orElseThrow(()-> new TrackerEntityNotFoundException(
                        String.format("No Model found with the provided ID: %s", modelId)
                ));

        if (trackerModel.isEquivalentTo(newTrackerModel)){
            System.out.println(" No changes match for both old Model and new Model brand !");

        }else{
            mergeTrackerModel(trackerModel,newTrackerModel);
            this.trackerModelRepository.save(trackerModel);
        }

        return this.trackerModelMapper.toTrackerModelResponse(trackerModel);
    }



    public void mergeTrackerModel(TrackerModel tackerModel, TrackerModelRequest newTrackerModelRequest){

        // Check each field for equality
        if(!tackerModel.getModelName().equals(newTrackerModelRequest.modelName())){
            tackerModel.setModelName(newTrackerModelRequest.modelName());
        }
        if(!tackerModel.getNetworkType().equals(newTrackerModelRequest.networkType())){
            tackerModel.setNetworkType(newTrackerModelRequest.networkType());
        }

        if(!tackerModel.getBatteryLife().equals(newTrackerModelRequest.batteryLife())){
            tackerModel.setBatteryLife(newTrackerModelRequest.batteryLife());
        }

        if(!tackerModel.getFeatures().equals(newTrackerModelRequest.features())){
            tackerModel.setFeatures(newTrackerModelRequest.features());
        }

        if(!tackerModel.getBrand().getId().equals(newTrackerModelRequest.brand())){

            TrackerBrand trackerBrand =  this.trackerBrandRepository.findById(newTrackerModelRequest.brand())
                    .orElseThrow(()-> new TrackerEntityNotFoundException(String.format(
                            "No Model found with the provided ID: %s", newTrackerModelRequest.brand()
                    )));
            tackerModel.setBrand(trackerBrand);
        }

    }


    public boolean deleteTrackerModelById(String modelId) {
       if( this.trackerModelRepository.existsById(modelId) ){
           this.trackerModelRepository.deleteById(modelId);
           return true;
       } else {
        throw new TrackerEntityNotFoundException("Brand not found with ID: " + modelId);
    }
    }
}
