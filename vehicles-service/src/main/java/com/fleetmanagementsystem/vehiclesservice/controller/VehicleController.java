package com.fleetmanagementsystem.vehiclesservice.controller;


import com.fleetManagementSystem.commons.driver.model.Driver;
import com.fleetManagementSystem.commons.position.dto.PositionVlResponse;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleRequest;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleResponse;
import com.fleetManagementSystem.commons.vehicle.model.TrackingResponse;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.vehiclesservice.mapper.CustomResponse;
import com.fleetmanagementsystem.vehiclesservice.mapper.VehicleMapper;
import com.fleetmanagementsystem.vehiclesservice.service.VehicleService;
import com.fleetmanagementsystem.vehiclesservice.utils.ResponseTypeEnum;
import jakarta.validation.Valid;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
//@CrossOrigin(origins = "http://localhost:5173") // Enable CORS for this controller
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;


    @PostMapping("")
    public ResponseEntity<CustomResponse<VehicleResponse>>  addVehicle(
            @RequestBody @Valid VehicleRequest vehicleRequest
    ){
        CustomResponse customResponse =
                new CustomResponse<>(
                        "Vehicle added successfuly",
                        ResponseTypeEnum.SUCCESS,
                        vehicleService.addVehicle(
                                vehicleMapper.toVehicle(vehicleRequest)
                        )
                );

        return  ResponseEntity.ok(customResponse);
    }

    @PostMapping("/assign_driver/{vehicleId}")
    public ResponseEntity<CustomResponse<VehicleResponse>> assignVehicle(
            @PathVariable("vehicleId") String vehicleId,
            @RequestBody @Valid String driverId
    ){
        CustomResponse customResponse =
                new CustomResponse<>(
                        "Vehicle assigned driver successfully",
                        ResponseTypeEnum.SUCCESS,
                        vehicleService.assignDriver(vehicleId,driverId)
                );

        return  ResponseEntity.ok(customResponse);
    }

    @PostMapping("/unassign_driver/{vehicleId}")
    public ResponseEntity<CustomResponse<VehicleResponse>> unassignVehicle(
            @PathVariable("vehicleId") String vehicleId,
            @RequestBody @Valid String driverId
    ){
        CustomResponse customResponse =
                new CustomResponse<>(
                        "Vehicle unassigned driver successfully",
                        ResponseTypeEnum.SUCCESS,
                        vehicleService.unassignDriver(vehicleId,driverId)
                );

        return  ResponseEntity.ok(customResponse);
    }

    @GetMapping()
    public ResponseEntity<CustomResponse<List<VehicleResponse>>> getAllVehicles(){
        System.out.println(" Request arrive to ms vehicle");
        CustomResponse customResponse = new CustomResponse(
                "Result retrieved successfully",
                ResponseTypeEnum.SUCCESS,
                this.vehicleService.getAllVehicles()
        );
        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/organization/{organization}")
    public ResponseEntity<CustomResponse<List<VehicleResponse>>> getAllVehiclesByOrganization(@PathVariable String organization){
        CustomResponse customResponse = new CustomResponse(
                "Result retrieved successfully",
                ResponseTypeEnum.SUCCESS,
                this.vehicleService.getAllVehiclesByOrganization(organization)
        );
        return ResponseEntity.ok(customResponse);
    }
    

    @GetMapping("/byLicensePlate/{licensePlate}")
    public ResponseEntity<CustomResponse<VehicleResponse>> getVehicleByLicensePlate(@PathVariable("licensePlate") String licensePlate ){
        CustomResponse customResponse = new CustomResponse(
                "Result retrieved successfully",
                ResponseTypeEnum.SUCCESS,
                this.vehicleService.getVehicleByLicensePlate(licensePlate)
        );
        return ResponseEntity.ok(customResponse);
    }



    @PutMapping("/{vehicleId}")
    public ResponseEntity<CustomResponse<VehicleResponse>> putVehicleById(
            @PathVariable("vehicleId") String vehicleId,
            @RequestBody @Valid VehicleRequest vehicleRequest
    ){

        return ResponseEntity.ok(new CustomResponse<>(
                "vehicle updated with success",
                ResponseTypeEnum.SUCCESS,
                this.vehicleService.putVehicleById(vehicleId, vehicleRequest)
        ));
    }


    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<CustomResponse<String>> deleteVehicleById(@PathVariable("vehicleId") String vehicleId){

        this.vehicleService.deleteVehicleById(vehicleId);
        return ResponseEntity.ok(new CustomResponse<>(
                "Vehicle deleted successfully" ,
                ResponseTypeEnum.SUCCESS ,
                vehicleId  // No specific data to return after deletion
        ));
    }

    @GetMapping("/imei/{imei}")
    public ResponseEntity<Vehicle> getVehicleByImei(@PathVariable String imei) {
        Vehicle vehicle = vehicleService.findByImei(imei);
        return ResponseEntity.ok(vehicle);
    }


    @GetMapping("/history")
    public ResponseEntity<CustomResponse<TrackingResponse>> getPositionVehicle(@QueryParam("vehicleID") String vehicleID , @QueryParam("startDate") String startDateTime, @QueryParam("endDate") String endDateTime){


        TrackingResponse trackingResponse = this.vehicleService.getPositionVehicle(vehicleID,startDateTime,endDateTime);
        return ResponseEntity.ok(new CustomResponse<>(
                "Vehicle Position retrieved successfully" ,
                ResponseTypeEnum.SUCCESS ,
                trackingResponse  // No specific data to return after deletion
        ));
    }

    @PutMapping("/updateLastPosition/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicleLastPosition(
            @PathVariable("vehicleId") String vehicleId,
            @RequestBody @Valid PositionVlResponse position
    ){
        return ResponseEntity.ok(this.vehicleService.updateVehicleLastPosition(vehicleId, position));
    }

}
