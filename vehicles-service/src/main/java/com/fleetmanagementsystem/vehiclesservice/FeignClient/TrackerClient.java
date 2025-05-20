package com.fleetmanagementsystem.vehiclesservice.FeignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "trackerClient", url = "http://localhost:8090/api/v1/trackers") // Replace with tracker URL
public interface TrackerClient {

    @PutMapping("/association/{trackerId}")
    public ResponseEntity<String> associatedTracker(
            @RequestHeader("Authorization") String token,
            @PathVariable("trackerId") String trackerId,
            @RequestBody @Valid Boolean associationValue
    );


}
