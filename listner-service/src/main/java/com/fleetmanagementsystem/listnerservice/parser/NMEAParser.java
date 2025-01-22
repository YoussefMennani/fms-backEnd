package com.fleetmanagementsystem.listnerservice.parser;


import com.fleetManagementSystem.commons.position.model.Metrics;
import com.fleetManagementSystem.commons.position.model.Position;

import java.util.Date;

public class NMEAParser {

    public static Position parseNMEA(String nmeaString) {
        // Split the NMEA string by commas (assuming CSV format)
        String[] parts = nmeaString.split(",");

        if (parts.length < 14) {
            // Handle incomplete data by returning null or throwing an error
            System.out.println("Invalid NMEA string: not enough data.");
            return null;
        }

        try {
            // Parse the latitude, longitude, speed, IMEI, and timestamp
            double latitude = parseDouble(parts[0]);
            double longitude = parseDouble(parts[1]);
            double altitude = parseDouble(parts[2]);
            double heading = parseDouble(parts[3]);
            double speed = parseDouble(parts[4]);
            String imei = parseString(parts[5]);
            long timestamp = parseLong(parts[6]);


            // Parse VehicleMetrics data
            Metrics metrics = new Metrics(
                    parseDouble(parts[7]),   // Engine Temperature
                    parseDouble(parts[8]),   // Coolant Temperature
                    parseDouble(parts[9]),   // Fuel Level
                    parseInt(parts[10]),      // Engine RPM
                    parseDouble(parts[11]),   // Battery Voltage
                    parseBoolean(parts[12]), // Check Engine Light
                    parseDouble(parts[13]),  // Oil Pressure
                    parseDouble(parts[14]),  // Tire Pressure
                    parseBoolean(parts[15])  // Engine Running
            );


            return new Position(latitude, longitude, imei, speed, timestamp,altitude,heading, metrics,null,null,new Date(), new Date());

        } catch (Exception e) {
            System.out.println("Error parsing NMEA string: " + e.getMessage());
            return null;
        }
    }

    // Helper methods to handle null or empty values
    private static double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;  // Default value
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;  // Default value in case of invalid number format
        }
    }

    private static long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return 0L;  // Default value
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;  // Default value in case of invalid number format
        }
    }

    private static int parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return 0;  // Default value
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;  // Default value in case of invalid number format
        }
    }

    private static boolean parseBoolean(String value) {
        if (value == null || value.isEmpty()) {
            return false;  // Default value
        }
        return value.equalsIgnoreCase("true");
    }

    private static String parseString(String value) {
        if (value == null || value.isEmpty()) {
            return "Unknown";  // Default value
        }
        return value;
    }

//    public static void main(String[] args) {
//        // Test NMEA String
//        String nmeaString = "31.498688,-8.832336,60.0,123456789012345,1700000000000,90.0,85.0,75.0,3000,12.6,true,40.5,32.0,true";
//
//        Position gpsPosition = parseNMEA(nmeaString);
//
//        if (gpsPosition != null) {
//            System.out.println(gpsPosition);
//        } else {
//            System.out.println("Failed to parse NMEA string.");
//        }
//    }
}