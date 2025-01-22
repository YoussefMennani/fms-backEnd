package com.fleetmanagementsystem.positionservice.controller;


import com.fleetManagementSystem.commons.enums.ResponseTypeEnum;
import com.fleetManagementSystem.commons.mapper.CustomResponse;
import com.fleetManagementSystem.commons.position.dto.PositionResponse;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetmanagementsystem.positionservice.service.PositionService;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/positions")
//@CrossOrigin("*") // Enable CORS for this controller
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/findPositions")
    public ResponseEntity<List<Position>> findPositionsByVehicleAndDateRange(@QueryParam("vehicleID") String vehicleID, @QueryParam("startDateTime") String startDateTime , @QueryParam("endDateTime") String endDateTime){
          Long timestampStart = Instant.parse(startDateTime+":00Z").toEpochMilli();
          Long timestampEnd = Instant.parse(endDateTime+":00Z").toEpochMilli();

        List<Position> positionResponseList = this.positionService.findPositionsByVehicleAndDateRange(vehicleID,timestampStart,timestampEnd);

        return ResponseEntity.ok(
                positionResponseList
        );

    }

    @GetMapping("/{positionId}")
    public ResponseEntity<Position> findPositionById(@PathVariable String positionId){
        Position position = this.positionService.findPositionById(positionId);
        return  ResponseEntity.ok(position);
    }



}
