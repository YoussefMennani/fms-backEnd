package com.fleetmanagementsystem.positionservice.service;


import com.fleetManagementSystem.commons.exception.PositionNotFoundException;
import com.fleetManagementSystem.commons.position.dto.PositionResponse;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetmanagementsystem.positionservice.Repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    public List<Position> findPositionsByVehicleAndDateRange(String vehicleID, Long timestampStart, Long timestampEnd) {
        // Query the database for matching positions
        List<Position> positions = positionRepository.findByVehicleIDAndTimestampBetween(vehicleID, timestampStart, timestampEnd);
        return positions;
    }

    public Position findPositionById(String positionId) {
        Position position = positionRepository.findById(positionId).orElseThrow(()-> new PositionNotFoundException("position not found"));
        return position;
    }
}
