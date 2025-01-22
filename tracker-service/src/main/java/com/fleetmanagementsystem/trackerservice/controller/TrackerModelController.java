package com.fleetmanagementsystem.trackerservice.controller;


import com.fleetManagementSystem.commons.tracker.dto.TrackerModelRequest;
import com.fleetManagementSystem.commons.tracker.dto.TrackerModelResponse;
import com.fleetmanagementsystem.trackerservice.mapper.CustomResponse;
import com.fleetmanagementsystem.trackerservice.service.TrackerModelService;
import com.fleetManagementSystem.commons.enums.ResponseTypeEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trackers/models")

//@CrossOrigin(origins = "http://localhost:5173") // Enable CORS for this controller
public class TrackerModelController {

    private final TrackerModelService trackerModelService;



    @GetMapping()
    public ResponseEntity<CustomResponse<List<TrackerModelResponse>>> getAllBrands(){
        CustomResponse customResponse = new CustomResponse(
                "Result retrieved successfully",
                ResponseTypeEnum.SUCCESS,
                this.trackerModelService.getAllTrackerModel()
                );
        return ResponseEntity.ok(customResponse);
    }


    @PostMapping
    public ResponseEntity<CustomResponse<TrackerModelResponse>> createTrackerBrand(
            @RequestBody @Valid TrackerModelRequest trackerModelRequest
    ){
        return ResponseEntity.ok(
                new CustomResponse<>(
                        "model brand created with success",
                        ResponseTypeEnum.SUCCESS,
                        this.trackerModelService.createTrackerModel(trackerModelRequest)
                )
        );
    }


    @GetMapping("/{brandId}")
    public ResponseEntity<CustomResponse<TrackerModelResponse>> getTrackerModelById(
            @PathVariable("brandId") String brandId
    ){
        CustomResponse customResponse = new CustomResponse(
                "Result retrieved successfully",
                ResponseTypeEnum.SUCCESS,
                this.trackerModelService.findTrackerModelById(brandId)
        );
        return ResponseEntity.ok(customResponse);
    }


    @PutMapping("/{modelId}")
    public ResponseEntity<CustomResponse<TrackerModelResponse>> putTrackerModelById(
            @PathVariable("modelId") String brandId,
            @RequestBody @Valid TrackerModelRequest modelRequest
    ){

        return ResponseEntity.ok(new CustomResponse<>(
                "brand updated with success",
                ResponseTypeEnum.SUCCESS,
                this.trackerModelService.putTrackerModelById(brandId, modelRequest)
        ));
    }


    @DeleteMapping("/{modelId}")
    public ResponseEntity<CustomResponse<String>> deleteBrandModelById(@PathVariable("modelId") String modelId){

        this.trackerModelService.deleteTrackerModelById(modelId);
        return ResponseEntity.ok(new CustomResponse<>(
                "model deleted successfully" ,
                ResponseTypeEnum.SUCCESS ,
                modelId  // No specific data to return after deletion
        ));
    }
}
