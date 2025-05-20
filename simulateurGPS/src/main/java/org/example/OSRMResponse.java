package org.example;

import java.util.List;

public class OSRMResponse {
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        private double distance;
        private double duration;
        private Geometry geometry;

        public double getDistance() {
            return distance;
        }

        public double getDuration() {
            return duration;
        }

        public Geometry getGeometry() {
            return geometry;
        }
    }

    public static class Geometry {
        private String type;
        private List<List<Double>> coordinates;

        public String getType() {
            return type;
        }

        public List<List<Double>> getCoordinates() {
            return coordinates;
        }
    }
}
