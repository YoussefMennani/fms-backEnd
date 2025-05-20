package org.example;

import com.fleetManagementSystem.commons.position.model.Metrics;
import com.fleetManagementSystem.commons.position.model.Position;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;

public class GPSSimulator {

    private static final String HOST = "localhost"; // GPS listener host
    private static final int PORT = 5000;           // GPS listener port
    private static final int INTERVAL = 10000;       // Interval between updates in milliseconds
    private static final int RECONNECT_DELAY = 5000; // Wait 5 seconds before reconnecting
    private static final int SOCKET_TIMEOUT = 5000;  // Timeout for detecting disconnection (5 seconds)

    private static Gson gson = new Gson(); // Initialize Gson for JSON conversion

    public static void main(String[] args) {


        List<GeoGraphicPath> geoGraphicPathsList = new ArrayList<>();
        geoGraphicPathsList.add(new GeoGraphicPath(31.497261948861023, -8.831443685480764,33.36654476012362,-4.71405526020655,"545454545454545"));
        geoGraphicPathsList.add(new GeoGraphicPath(33.6935, -6.3138,33.8955,-5.5412,"124338493483748"));


        geoGraphicPathsList.forEach((GeoGraphicPath geoGraphicPath )->{

            new Thread(()->{
                try {
                    String jsonResponse = geoGraphicPath.fetchRouteData();
                    OSRMResponse osrmResponse = StaticFields.parseRouteData(jsonResponse);

                    if (osrmResponse != null && !osrmResponse.getRoutes().isEmpty()) {
                        OSRMResponse.Route route = osrmResponse.getRoutes().get(0);
                        System.out.println("Distance: " + route.getDistance());
                        System.out.println("Duration: " + route.getDuration());
                        System.out.println("Coordinates: " + route.getGeometry().getCoordinates());


                        Queue<List<Double>> coordinatesQueue = new LinkedList<>();
                        route.getGeometry().getCoordinates().forEach(coord -> coordinatesQueue.offer(coord));

                            while (true) {
                                try (Socket socket = new Socket(HOST, PORT);
                                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                                    System.out.println("Connected to GPS Listener at " + HOST + ":" + PORT);

                                    // Set the socket read timeout
                                    socket.setSoTimeout(SOCKET_TIMEOUT);

                                    // Send the IMEI once upon connection
                                    String gpsImei = geoGraphicPath.imei();
                                            //StaticFields.generateRandomIMEI();
                                    out.println(gpsImei);
                                    out.flush();


                                        while (!socket.isClosed()) {
                                            // Generate random GPS coordinates
                                            List<Double> coord = coordinatesQueue.poll();

                                            // Create a GPSPosition object
                                            if (coord != null && coordinatesQueue.size() > 0) {
                                                // Create an instance of VehicleMetrics
                                                Metrics metrics = new Metrics(
                                                        90.0,   // Engine Temperature in Celsius
                                                        85.0,   // Coolant Temperature in Celsius
                                                        75.0,   // Fuel Level in percentage
                                                        3000,   // Engine RPM
                                                        12.6,   // Battery Voltage in Volts
                                                        false,  // Check Engine Light status
                                                        40.5,   // Oil Pressure in kPa
                                                        32.0,   // Tire Pressure in psi
                                                        true    // Is the engine running
                                                );

                                                // Print the VehicleMetrics object
                                                System.out.println(metrics);
                                                Position gpsData = new Position(coord.get(1),coord.get(0),gpsImei,60.0, new Date().getTime(),100.5,90.0,metrics,null,null);

                                                //break; // Exit if queue is empty


                                                // Convert the GPSPosition object to JSON
                                                String jsonGpsData = gson.toJson(gpsData);

                                                // Send the JSON position data to the listener
                                                out.println(jsonGpsData);
                                                out.flush();

                                                System.out.println("Sent position data: " + jsonGpsData + " for IMEI:"+gpsImei);

                                                // Try reading the server's acknowledgment, which will timeout if the server is down
                                                try {
                                                    String serverResponse = in.readLine();
                                                    if (serverResponse == null) {
                                                        throw new SocketException("Connection closed by server.");
                                                    }
                                                    System.out.println("Received from server: " + serverResponse);

                                                }
                                                catch (IOException e) {
                                                    System.err.println(" IOExceptionNo response from server within timeout period, reconnecting...");
                                                    break;
                                                }
                                            } else {
                                                System.out.println("No more positions to send.");
                                            }
                                            // Wait for the specified interval before sending the next update
                                            try {
                                                Thread.sleep(INTERVAL);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
//                                    });

                                } catch (IOException e) {
                                    System.err.println("Connection error: " + e.getMessage());
                                    System.out.println("Attempting to reconnect in " + RECONNECT_DELAY / 1000 + " seconds...");
                                    // Wait before attempting to reconnect
                                    try {
                                        Thread.sleep(RECONNECT_DELAY);
                                    } catch (InterruptedException interruptedException) {
                                        System.err.println("Reconnection wait interrupted: " + interruptedException.getMessage());
                                    }
                                }
                            }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();


        });



    }
}
