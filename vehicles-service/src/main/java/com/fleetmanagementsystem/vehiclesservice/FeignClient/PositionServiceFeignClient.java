package com.fleetmanagementsystem.vehiclesservice.FeignClient;

import com.fleetManagementSystem.commons.position.model.Position;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "position-service", url = "http://localhost:8092/api/v1/positions")
public interface PositionServiceFeignClient  {

    @GetMapping("/findPositions")
    ResponseEntity<List<Position>> getPositions(@RequestParam("vehicleID") String vehicleID,
                                                @RequestParam("startDateTime") String startDateTime,
                                                @RequestParam("endDateTime") String endDateTime);

}
