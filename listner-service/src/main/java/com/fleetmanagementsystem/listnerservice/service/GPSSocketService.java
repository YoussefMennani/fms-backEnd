package com.fleetmanagementsystem.listnerservice.service;

import com.fleetManagementSystem.commons.driver.dto.MinimalDriver;
import com.fleetManagementSystem.commons.driver.model.Driver;
import com.fleetManagementSystem.commons.exception.VehicleNotFoundException;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.dto.MinimalVehicle;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.listnerservice.FeignClient.VehicleFeignClient;
import com.fleetmanagementsystem.listnerservice.parser.NMEAParser;
import com.fleetmanagementsystem.listnerservice.repository.PositionRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class GPSSocketService {

    //@Value("${gps.socket.port}")
    private int port = 5000;

//    @Value("${gps.socket.max-threads}")
    private int maxThreads = 10;

    private final ExecutorService executorService;
    private final PositionRepository positionRepository;
    private final PositionProducer positionProducer;

    private final AlertProducer alertProducer;
    private final VehicleService vehicleService; // Injected here
    private final Gson gson;



    @Autowired
    public GPSSocketService(PositionRepository positionRepository,
                            PositionProducer positionProducer,
                            VehicleService vehicleService,
                            AlertProducer alertProducer
                            ) {
        this.positionRepository = positionRepository;
        this.positionProducer = positionProducer;
        this.vehicleService = vehicleService;
        this.alertProducer = alertProducer;
        this.gson = new Gson();
        this.executorService = Executors.newFixedThreadPool(maxThreads);
    }

    public void startSocketServer() {
        executorService.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("GPS Socket Server started on port " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());
                    executorService.submit(new ClientHandler(clientSocket));
                }
            } catch (Exception e) {
                System.err.println("Error in GPS Socket Server: " + e.getMessage());
            }
        });
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private String imei;
        private Vehicle vehicle;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // Read IMEI from the client
                String imei = in.readLine();
                if (imei != null) {
                    this.imei = imei;
                    System.out.println("Received IMEI: " + imei);

                    Vehicle vehicleTarget = vehicleService.findVehicleByImei(imei)
                            .orElseThrow(() -> new VehicleNotFoundException("Vehicle Not Found"));
//                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++"+vehicleTarget.getBrandVehicle());
                    this.vehicle = vehicleTarget;
                    System.out.println("Vehicle Found: " + vehicle);
                } else {
                    System.out.println("IMEI not received; client might have disconnected.");
                    return;
                }

                // Read and process position data
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received data: " + inputLine);

                    try {
                        // Parse the JSON into a Position object
                        Position position = NMEAParser.parseNMEA(inputLine);
                        position.setTimestamp( new Date().getTime());
                        System.out.println("position arrived : "+position);

                        //Vehicle DATA
                        MinimalVehicle minimalVehicle = MinimalVehicle.builder()
                                .id(vehicle.getId().toString())
                                .licensePlate(vehicle.getLicensePlate())
                                .build();
                        position.setVehicle(minimalVehicle);

                        if(vehicle.getCurrentDriver() != null){
                            //DRIVER DATA
                            MinimalDriver minimalDriver = MinimalDriver.builder()
                                    .id(vehicle.getCurrentDriver().getId())
                                    .firstName(vehicle.getCurrentDriver().getFirstName())
                                    .lastName(vehicle.getCurrentDriver().getLastName())
                                    .phoneNumber(vehicle.getCurrentDriver().getPhoneNumber())
                                    .organization(vehicle.getOrganization().getName())
                                    .build();
                            position.setDriver(minimalDriver);
                        }else{
                            position.setDriver(null);
                        }

                        // Save to MongoDB
                        Position savedPosition = positionRepository.save(position);

                        System.out.println("Position saved to MongoDB: " + position);

                        // Send to Kafka anomaly detection
                        positionProducer.sendPosition("anomaly-detection", position);

                        // Send to Kafka position
                        positionProducer.sendPosition("position-updates", position);
                        // Send to Kafka
                        alertProducer.sendPosition("alert-topic", position);

                        // update lastPosition
                        vehicleService.updateVehicleLastPosition(vehicle.getId(),position);
                        // Send acknowledgment back to the client
                        out.println("ACK: Position received");

                    } catch (Exception e) {
                        System.err.println("Failed to process GPS data: " + e.getMessage());
                        out.println("ERROR: Invalid data");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected.");
                } catch (Exception e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }
}
