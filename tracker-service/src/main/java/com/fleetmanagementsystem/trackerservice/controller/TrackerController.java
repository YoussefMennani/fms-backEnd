package com.fleetmanagementsystem.trackerservice.controller;


import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.tracker.dto.TrackerModelResponse;
import com.fleetManagementSystem.commons.tracker.dto.TrackerRequest;
import com.fleetManagementSystem.commons.tracker.dto.TrackerResponse;
import com.fleetmanagementsystem.trackerservice.FeignClient.OrganizationClient;
import com.fleetmanagementsystem.trackerservice.mapper.CustomResponse;
import com.fleetmanagementsystem.trackerservice.service.TrackerModelService;
import com.fleetmanagementsystem.trackerservice.service.TrackerService;
import com.fleetManagementSystem.commons.enums.ResponseTypeEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trackers")

//@CrossOrigin(origins = "http://localhost:5173") // Enable CORS for this controller
public class TrackerController {

    private final TrackerModelService trackerModelService;
    private final TrackerService trackerService;
    private final OrganizationClient organizationClient;


    @GetMapping()
    public ResponseEntity<CustomResponse<List<TrackerResponse>>> getAllTrackers(){
        CustomResponse customResponse = new CustomResponse(
                "Result retrieved successfully",
                ResponseTypeEnum.SUCCESS,
                this.trackerService.getAllTrackerModel()
                );
        return ResponseEntity.ok(customResponse);
    }


    @PostMapping
    public ResponseEntity<CustomResponse<TrackerResponse>> createTrackerBrand(
            @RequestBody @Valid TrackerRequest trackerRequest
    ){
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();

        // Fetch the organization details
        Organization organization = this.organizationClient.getOrganizationById(token, trackerRequest.organization()).getBody();


        return ResponseEntity.ok(
                new CustomResponse<>(
                        "tracker created with success",
                        ResponseTypeEnum.SUCCESS,
                        this.trackerService.createTracker(trackerRequest,organization)
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


    @PutMapping("/{trackerId}")
    public ResponseEntity<CustomResponse<TrackerResponse>> putTrackerById(
            @PathVariable("trackerId") String trackerId,
            @RequestBody @Valid TrackerRequest trackerRequest
    ){

        return ResponseEntity.ok(new CustomResponse<>(
                "tracker updated with success",
                ResponseTypeEnum.SUCCESS,
                this.trackerService.putTrackerModelById(trackerId, trackerRequest)
        ));
    }


    @DeleteMapping("/{trackerId}")
    public ResponseEntity<CustomResponse<String>> deleteTrackerById(@PathVariable("trackerId") String trackerId){

        this.trackerService.deleteTrackerById(trackerId);
        return ResponseEntity.ok(new CustomResponse<>(
                "Tracker deleted successfully" ,
                ResponseTypeEnum.SUCCESS ,
                trackerId  // No specific data to return after deletion
        ));
    }

    @PutMapping("/association/{trackerId}")
    public ResponseEntity<String> associatedTracker(
            @PathVariable("trackerId") String trackerId,
            @RequestBody @Valid Boolean associationValue
    ){
        this.trackerService.associatedTracker(trackerId, associationValue);

        return ResponseEntity.ok(
                "tracker association updated with success"
        );
    }


}
