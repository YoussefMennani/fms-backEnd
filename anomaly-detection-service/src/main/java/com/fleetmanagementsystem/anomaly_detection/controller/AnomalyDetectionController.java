package com.fleetmanagementsystem.anomaly_detection.controller;

import com.fleetmanagementsystem.anomaly_detection.model.AnomalyDetection;
import com.fleetmanagementsystem.anomaly_detection.service.AnomalyDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/anomaly_detection")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AnomalyDetectionController {

    private final AnomalyDetectionService anomalyDetectionService;

    @GetMapping("/gradient_chart")
    ResponseEntity<Double> get_gradient_chart(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        System.out.println("Request for start date :" + startDate + " and end date:" + endDate);
        Double result = anomalyDetectionService.get_gradient_chart(startDate,endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAnomalyData")
    ResponseEntity<List<AnomalyDetection>> getAnomalyData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        System.out.println("Request for start date :" + startDate + " and end date:" + endDate);
        List<AnomalyDetection> result = anomalyDetectionService.getAnomalyData(startDate,endDate);
        return ResponseEntity.ok(result);
    }


}
