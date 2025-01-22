package com.fleetmanagementsystem.anomaly_detection.feignClient;


import com.mongodb.client.model.geojson.Position;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "position-service", url = "http://localhost:8092/api/v1/positions")
public interface PositionFeignClient {

    @GetMapping("/{positionId}")
    Position findPositionById(@PathVariable String positionId);
}
