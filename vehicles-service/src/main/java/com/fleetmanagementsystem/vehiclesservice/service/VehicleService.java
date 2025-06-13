package com.fleetmanagementsystem.vehiclesservice.service;

import com.fleetManagementSystem.commons.driver.model.Driver;
import com.fleetManagementSystem.commons.exception.PositionNotFoundException;
import com.fleetManagementSystem.commons.exception.VehicleNotFoundException;
import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.position.dto.PositionResponse;
import com.fleetManagementSystem.commons.position.dto.PositionVlResponse;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleRequest;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleResponse;
import com.fleetManagementSystem.commons.vehicle.model.TrackingResponse;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.vehiclesservice.FeignClient.DriverClient;
import com.fleetmanagementsystem.vehiclesservice.FeignClient.OrganizationClient;
import com.fleetmanagementsystem.vehiclesservice.FeignClient.PositionServiceFeignClient;
import com.fleetmanagementsystem.vehiclesservice.FeignClient.TrackerClient;
import com.fleetmanagementsystem.vehiclesservice.exception.VehicleEntityNotFoundException;
import com.fleetmanagementsystem.vehiclesservice.mapper.VehicleMapper;
import com.fleetmanagementsystem.vehiclesservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleMapper vehicleMapper;
    private final VehicleRepository vehicleRepository;

    private final OrganizationClient organizationClient;
    private final TrackerClient trackerClient;
    private final PositionServiceFeignClient positionServiceFeignClient;

    private final DriverClient driverClient;
    public VehicleResponse addVehicle(Vehicle vehicle){
        Vehicle vehicleDB = vehicleRepository.save(vehicle);
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();
        this.trackerClient.associatedTracker(token, vehicle.getTracker().getTrackerId(),true);
        VehicleResponse vehicleResponse = vehicleMapper.toResponseVehicle(vehicleDB);
        return vehicleResponse;
    }


    public List<VehicleResponse> getAllVehicles() {
        List<VehicleResponse> vehicleResponseList = new ArrayList<>();
        try {
            // Extract the token from the security context
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String token = "Bearer " + authentication.getToken().getTokenValue();
            System.out.println(" ___________token_____________ : "+token);

            System.out.println("__________try to get Orgs__________ : "+token);
            List<Organization> organizationList = organizationClient.getRootOrganizations(token);

            System.out.println("__________try to get organizationList________________ : "+organizationList);

            // Extract organization IDs from the organizationList
            Set<String> organizationIds = organizationList.stream()
                    .map(Organization::getId)
                    .collect(Collectors.toSet());

            // Fetch all vehicles
            List<Vehicle> vehicleList = vehicleRepository.findAll();

            // Filter vehicles based on organization IDs
            List<Vehicle> filteredVehicleList = vehicleList.stream()
                    .filter(vehicle -> organizationIds.contains(vehicle.getOrganization().getId()))
                    .toList();

            // Map filtered vehicles to VehicleResponse
            vehicleResponseList = filteredVehicleList.stream()
                    .map(vehicle -> vehicleMapper.toResponseVehicle(vehicle))
                    .toList();

//            return vehicleResponseList;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }finally {
            return vehicleResponseList;
        }

    }

    public List<VehicleResponse> getAllVehiclesByOrganization( String organization) {
        List<Vehicle> vehicleList = vehicleRepository.findByOrganization(organization).orElseThrow(()->new VehicleNotFoundException("Not Vehicle Found with organization"+organization));
        List<VehicleResponse> vehicleResponseList = vehicleList.stream().map(
                (vehicle ->  vehicleMapper.toResponseVehicle(vehicle))).toList();
        return vehicleResponseList;
    }


    public VehicleResponse putVehicleById(String vehicleId, VehicleRequest vehicleRequest) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();
       Vehicle vehicle = this.vehicleRepository.findById(vehicleId)
               .orElseThrow(()->new VehicleEntityNotFoundException(
                       String.format("No Vehicle found with the provided ID: %s", vehicleId)
               ));

       if(vehicle.getTracker().getTrackerId() != vehicleRequest.getTracker().getTrackerId()){
           this.trackerClient.associatedTracker(token,vehicleRequest.getTracker().getTrackerId(),false);
           this.trackerClient.associatedTracker(token,vehicle.getTracker().getTrackerId(),true);

       }
        if (vehicle.isEquivalentTo(vehicleRequest)){
            System.out.println(" No changes Vehicle for both old and new !");

        }else{
            mergeVehicle(vehicle,vehicleRequest);
            this.vehicleRepository.save(vehicle);
        }

        return this.vehicleMapper.toResponseVehicle(vehicle);
    }

    public void mergeVehicle(Vehicle vehicle, VehicleRequest vehicleRequest){

        // Check each field for equality
        if(!vehicle.getLicensePlate().equals(vehicleRequest.getLicensePlate())){
            vehicle.setLicensePlate(vehicleRequest.getLicensePlate());
        }

        if(!vehicle.getModelVehicle().equals(vehicleRequest.getModelVehicle())){
            vehicle.setModelVehicle(vehicleRequest.getModelVehicle());
        }

        if(!vehicle.getBrandVehicle().equals(vehicleRequest.getBrandVehicle())){
            vehicle.setBrandVehicle(vehicleRequest.getBrandVehicle());
        }

        if(vehicle.getYear() == vehicleRequest.getYear()){
            vehicle.setYear(vehicleRequest.getYear());
        }

        if(!vehicle.getColor().equals(vehicleRequest.getColor())){
            vehicle.setColor(vehicleRequest.getColor());
        }

        if(!vehicle.getFuelType().equals(vehicleRequest.getFuelType())){
            vehicle.setFuelType(vehicleRequest.getFuelType());
        }

        if(!vehicle.getStatus().equals(vehicleRequest.getStatus())){
            vehicle.setStatus(vehicleRequest.getStatus());
        }

        if(!vehicle.getTracker().getTrackerId().equals(vehicleRequest.getTracker().getTrackerId())){
            vehicle.setTracker(vehicleRequest.getTracker());
        }

//        if(!vehicle.getOrganization().equals(vehicleRequest.getOrganization())){
//            vehicle.setOrganization(vehicleRequest.getOrganization());
//        }

        if(!vehicle.getImgPath().equals(vehicleRequest.getImgPath())){
            vehicle.setImgPath(vehicleRequest.getImgPath());
        }
    }

    public boolean deleteVehicleById(String vehicleId) {
        if( this.vehicleRepository.existsById(vehicleId) ){
            this.vehicleRepository.deleteById(vehicleId);
            return true;
        } else {
            throw new VehicleEntityNotFoundException("Tracker not found with ID: " + vehicleId);
        }
    }

    public TrackingResponse getPositionVehicle(String vehicleID, String startDateTime, String endDateTime) {
     Vehicle vehicle = this.vehicleRepository.findById(vehicleID).orElseThrow(()->new VehicleEntityNotFoundException("Vehicle not found"));
     VehicleResponse vehicleResponse = vehicleMapper.toResponseVehicle(vehicle);

        ResponseEntity<List<Position>> positionList = positionServiceFeignClient.getPositions(vehicleID,startDateTime,endDateTime);
        if (positionList.getStatusCode() != HttpStatusCode.valueOf(200))
            throw new PositionNotFoundException("probleme related to positions");

        TrackingResponse trackingResponse = TrackingResponse.builder()
                .vehicle(vehicleResponse)
                .positionList(positionList.getBody())
                .build();

    return trackingResponse;
    }

    public VehicleResponse getVehicleByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleEntityNotFoundException("Vehicle with license plate " + licensePlate + " not found"));


        return vehicleMapper.toResponseVehicle(vehicle);
    }

    public Vehicle findByImei(String imei) {
        Vehicle vehicle = vehicleRepository.findByTrackerImei(imei)
                .orElseThrow(() -> new VehicleEntityNotFoundException("Vehicle with imei " + imei + " not found"));

        return vehicle;
    }

    public VehicleResponse assignDriver(String vehicleId, String driverId) {
        try {

            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String token = "Bearer " + authentication.getToken().getTokenValue();

            Vehicle vehicle = this.vehicleRepository.findById(vehicleId).orElseThrow(()-> new VehicleEntityNotFoundException("Vehicle not found"));
//            ResponseEntity<Driver> driver = this.driverClient.getDriverById(driverId);
            ResponseEntity<Driver> driver = this.driverClient.updateAvailabilityDriver(token,driverId,false);

        if(!driver.getStatusCode().equals(HttpStatusCode.valueOf(200))){
            throw new RuntimeException("Driver Not Found Exception");
        }

            vehicle.setCurrentDriver(driver.getBody());
            this.vehicleRepository.save(vehicle);


            return this.vehicleMapper.toResponseVehicle(vehicle);
        }catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }

    public VehicleResponse unassignDriver(String vehicleId, String driverId) {
        try {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String token = "Bearer " + authentication.getToken().getTokenValue();

            Vehicle vehicle = this.vehicleRepository.findById(vehicleId).orElseThrow(()-> new VehicleEntityNotFoundException("Vehicle not found"));
//            ResponseEntity<Driver> driver = this.driverClient.getDriverById(driverId);
            ResponseEntity<Driver> driver = this.driverClient.updateAvailabilityDriver(token,driverId,true);

            if(!driver.getStatusCode().equals(HttpStatusCode.valueOf(200))){
                throw new RuntimeException("Driver Not Found Exception");
            }

            vehicle.setCurrentDriver(null);
            this.vehicleRepository.save(vehicle);


            return this.vehicleMapper.toResponseVehicle(vehicle);
        }catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }

    public Vehicle updateVehicleLastPosition(String vehicleId, PositionVlResponse position) {
        Vehicle vehicle = this.vehicleRepository.findById(vehicleId)
                .orElseThrow(()->new VehicleEntityNotFoundException(
                        String.format("No Vehicle found with the provided ID: %s", vehicleId)
                ));
//        PositionResponse positionResponse = PositionResponse.builder()
//                .latitude(position.getLatitude())
//                .longitude(position.getLongitude())
//                .imei(position.getImei())
//                .speed(position.getSpeed())
//                .timestamp(position.getTimestamp())
//                .altitude(position.getAltitude())
//                .heading(position.getHeading())
//                .metrics(position.getMetrics())
////                .vehicle(position.getVehicle())
////                .driver(position.getDriver())
//                .build();

          vehicle.setLastPosition(position);
            this.vehicleRepository.save(vehicle);


        return vehicle;
    }
}
