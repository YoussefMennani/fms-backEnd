package com.fleetmanagementsystem.anomaly_detection.model;

import com.fleetManagementSystem.commons.position.model.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

@Document(collection = "anomalies_detection")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnomalyDetection {

    @Id
    private String id;

    private Position position;

    private String jugement;

    @Field("tolerant_error")
    private double tolerantError;

    @Field("reconstruction_error")
    private double reconstructionError;

    private Map<String, Double> anomalies;

    private Date positionDate;

    private Date timestamp;

}
