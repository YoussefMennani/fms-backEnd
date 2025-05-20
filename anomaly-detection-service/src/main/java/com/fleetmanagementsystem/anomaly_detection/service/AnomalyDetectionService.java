package com.fleetmanagementsystem.anomaly_detection.service;

import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetmanagementsystem.anomaly_detection.model.AnomalyDetection;
import com.fleetmanagementsystem.anomaly_detection.repository.AnomalyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnomalyDetectionService {

    private final AnomalyRepository anomalyRepository;

    public Double get_gradient_chart(LocalDateTime startDate, LocalDateTime endDate){
        List<AnomalyDetection> anomalyDetectionList =  anomalyRepository.findByPositionDateBetween(startDate,endDate);
        Double resultAnomalyDetection = calculateMeanReconstructionError(anomalyDetectionList);
        return resultAnomalyDetection;
    }

    public double calculateMeanReconstructionError( List<AnomalyDetection> anomalies) {

        return anomalies.stream()
                .mapToDouble(AnomalyDetection::getReconstructionError)
                .average()
                .orElse(0.0); // Return 0.0 if there are no results
    }

    public List<AnomalyDetection> getAnomalyData(LocalDateTime startDate, LocalDateTime endDate) {
        List<AnomalyDetection> anomalyDetectionList =  anomalyRepository.findByPositionDateBetween(startDate,endDate);
         anomalyDetectionList.sort(Comparator.comparing(AnomalyDetection::getPositionDate).reversed());
        return anomalyDetectionList;
    }
}
