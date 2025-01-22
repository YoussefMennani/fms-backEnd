package com.fleetmanagementsystem.mqttsubscriberservice.FeignClient;

import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "vehicle-service", url = "http://localhost:8222", configuration = FeignAuthInterceptor.class)
public interface VehicleFeignClient {

    @GetMapping("/api/v1/vehicles/imei/{imei}")
    Vehicle getVehicleByImei(@PathVariable("imei") String imei);

    @PutMapping("/api/v1/vehicles/updateLastPosition/{vehicleId}")
    Vehicle updateVehicleLastPosition(
            @PathVariable("vehicleId") String vehicleId,
            @RequestBody @Valid Position position
    );
}
