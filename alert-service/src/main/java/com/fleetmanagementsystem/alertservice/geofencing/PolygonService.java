package com.fleetmanagementsystem.alertservice.geofencing;

import com.fleetManagementSystem.commons.geofence.model.Location;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolygonService {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public boolean isPointInPolygon(double pointLat, double pointLon, List<Location> polygonCoords) {
        // Create a Point geometry from the given latitude and longitude
        Point point = geometryFactory.createPoint(new Coordinate(pointLon, pointLat)); // [longitude, latitude]

        // Create the polygon from the list of coordinates
        List<Coordinate> coordinates = new ArrayList<>();
        for (Location coord : polygonCoords) {
            coordinates.add(new Coordinate(coord.getLng(), coord.getLat())); // [longitude, latitude]
        }

        // The first and last points should be the same to close the polygon
        coordinates.add(coordinates.get(0));

        // Create the Polygon geometry
        Polygon polygon = geometryFactory.createPolygon(coordinates.toArray(new Coordinate[0]));

        // Check if the point is inside the polygon
        return polygon.contains(point);
    }

}
