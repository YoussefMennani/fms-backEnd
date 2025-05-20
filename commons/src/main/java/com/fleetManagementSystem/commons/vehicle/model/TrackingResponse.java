package com.fleetManagementSystem.commons.vehicle.model;

import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
public record TrackingResponse(VehicleResponse vehicle, List<Position> positionList) {
}
