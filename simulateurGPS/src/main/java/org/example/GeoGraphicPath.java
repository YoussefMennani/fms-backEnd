package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public record GeoGraphicPath(double latStart, double longStart,double latEnd , double longEnd, String imei) {

    public  String fetchRouteData() throws IOException, InterruptedException {
        String osrmUrl = "https://router.project-osrm.org/route/v1/driving/" + this.longStart + "," + this.latStart
                + ";" + this.longEnd + "," + this.latEnd + "?geometries=geojson";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(osrmUrl)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch data: " + response.statusCode());
        }
    }

}
