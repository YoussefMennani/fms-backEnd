package com.fleetmanagementsystem.trackerservice.service;

import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.tracker.dto.TrackerBrandResponse;
import com.fleetManagementSystem.commons.tracker.dto.TrackerRequest;
import com.fleetManagementSystem.commons.tracker.dto.TrackerResponse;
import com.fleetmanagementsystem.trackerservice.FeignClient.OrganizationClient;
import com.fleetmanagementsystem.trackerservice.exception.TrackerEntityNotFoundException;
import com.fleetmanagementsystem.trackerservice.mapper.TrackerBrandMapper;
import com.fleetmanagementsystem.trackerservice.mapper.TrackerMapper;
import com.fleetManagementSystem.commons.tracker.model.TrackerBrand;
import com.fleetManagementSystem.commons.tracker.model.TrackerModel;
import com.fleetManagementSystem.commons.tracker.model.Tracker;
import com.fleetmanagementsystem.trackerservice.repository.TrackerBrandRepository;
import com.fleetmanagementsystem.trackerservice.repository.TrackerModelRepository;
import com.fleetmanagementsystem.trackerservice.repository.TrackerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TrackerService {

    private final TrackerBrandMapper trackerBrandMapper;
    private final TrackerBrandRepository trackerBrandRepository;

    private final TrackerModelRepository trackerModelRepository;
    private final TrackerRepository trackerRepository;
    private final TrackerMapper trackerMapper;

    private final OrganizationClient organizationClient;

    public TrackerResponse createTracker(TrackerRequest trackerRequest, Organization organization) {
        Tracker t =  trackerMapper.toTracker(trackerRequest);
        t.setOrganization(organization);
        var tracker = this.trackerRepository.save(t);
        return trackerMapper.toTrackerResponse(tracker);
    }

    public List<TrackerBrandResponse> findAllTrackerBrand() {
        return this.trackerBrandRepository
                .findAll()
                .stream()
                .map(this.trackerBrandMapper::fromTrackerBrand)
                .collect(Collectors.toList());
    }

    public TrackerBrandResponse findTrackerBrandById(String brandId) {
        return  this.trackerBrandRepository
                .findById(brandId)
                .map(this.trackerBrandMapper::fromTrackerBrand)
                .orElseThrow(()-> new TrackerEntityNotFoundException(String.format(
                        "No Brand found with the provided ID: %s", brandId
                )));
    }

//    public TrackerBrandResponse putTrackerBrandById(String brandId, TrackerBrandRequest newTrackerBrand) {
//
//        var brand =  this.trackerBrandRepository.findById(brandId)
//                .orElseThrow(()-> new TrackerEntityNotFoundException(
//                        String.format("No customer found with the provided ID: %s", brandId)
//                ));
//
//        if (brand.isEquivalentTo(newTrackerBrand)){
//            System.out.println(" No changes brand for both old Model and new Model brand !");
//
//        }else{
//            mergeTrackerBrand(brand,newTrackerBrand);
//            this.trackerRepository.save(brand);
//        }
//
//        return this.trackerBrandMapper.fromTrackerBrand(brand);
//    }


//    public void mergeTrackerBrand(TrackerBrand brand, TrackerBrandRequest trackerBrandRequest){
//
//        Optional.ofNullable(trackerBrandRequest.brandName())
//                .filter(StringUtils::isNotBlank)
//                .ifPresent(brand::setBrandName);
//
//        Optional.ofNullable(trackerBrandRequest.originCountry())
//                .filter(StringUtils::isNotBlank)
//                .ifPresent(brand::setOriginCountry);
//
//    }



    public List<TrackerResponse> getAllTrackerModel() {
        // Extract the token from the security context
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();

        // Fetch root organizations using the token
        List<Organization> organizationList = organizationClient.getRootOrganizations(token);

        // Extract organization IDs from the organizationList
        Set<String> organizationIds = organizationList.stream()
                .map(Organization::getId)
                .collect(Collectors.toSet());

        // Fetch all trackers
        List<Tracker> trackerList = this.trackerRepository.findAll();

        // Filter trackers based on organization IDs
        List<Tracker> filteredTrackerList = trackerList.stream()
                .filter(tracker -> organizationIds.contains(tracker.getOrganization().getId()))
                .toList();

        // Map filtered trackers to TrackerResponse
        List<TrackerResponse> trackerResponseList = filteredTrackerList.stream()
                .map(tracker -> this.trackerMapper.toTrackerResponse(tracker))
                .toList();

        return trackerResponseList;
    }
    public TrackerResponse putTrackerModelById(String trackerId, TrackerRequest trackerRequest) {
        Tracker trackerdb =  this.trackerRepository.findById(trackerId)
                .orElseThrow(()-> new TrackerEntityNotFoundException(
                        String.format("No Tracker found with the provided ID: %s", trackerId)
                ));

        if (trackerdb.isEquivalentTo(trackerRequest)){
            System.out.println(" No changes match for both old Model and new Model brand !");

        }else{
            mergeTrackerModel(trackerdb,trackerRequest);
            this.trackerRepository.save(trackerdb);
        }

        return this.trackerMapper.toTrackerResponse(trackerdb);

    }


    public void mergeTrackerModel(Tracker trackerdb, TrackerRequest trackerRequest){

        // Check each field for equality
        if(!trackerdb.getImei().equals(trackerRequest.imei())){
            trackerdb.setImei(trackerRequest.imei());
        }

        if(!trackerdb.getSimSerialNumber().equals(trackerRequest.simSerialNumber())){
            trackerdb.setSimSerialNumber(trackerRequest.simSerialNumber());
        }
        if(!trackerdb.getSimNumber().equals(trackerRequest.simNumber())){
            trackerdb.setSimNumber(trackerRequest.simNumber());
        }
        if(!trackerdb.getStatus().equals(trackerRequest.status())){
            trackerdb.setStatus(trackerRequest.status());
        }

        if(!trackerdb.getBrand().getId().equals(trackerRequest.brand())){

            TrackerBrand trackerBrand =  this.trackerBrandRepository.findById(trackerRequest.brand())
                    .orElseThrow(()-> new TrackerEntityNotFoundException(String.format(
                            "No Tracker found with the provided ID: %s", trackerRequest.brand()
                    )));
            trackerdb.setBrand(trackerBrand);
        }


        if(!trackerdb.getModel().getId().equals(trackerRequest.model())){

            TrackerModel trackerModel =  this.trackerModelRepository.findById(trackerRequest.model())
                    .orElseThrow(()-> new TrackerEntityNotFoundException(String.format(
                            "No Model found with the provided ID: %s", trackerRequest.model()
                    )));
            trackerdb.setModel(trackerModel);
        }


    }


    public boolean deleteTrackerById(String trackerId) {
        if( this.trackerRepository.existsById(trackerId) ){
            this.trackerRepository.deleteById(trackerId);
            return true;
        } else {
            throw new TrackerEntityNotFoundException("Tracker not found with ID: " + trackerId);
        }
    }

    public void associatedTracker(String trackerId, Boolean associationValue) {

        Tracker trackerdb =  this.trackerRepository.findById(trackerId)
                .orElseThrow(()-> new TrackerEntityNotFoundException(
                        String.format("No Tracker found with the provided ID: %s", trackerId)
                ));

        trackerdb.setVehicleAssociated(associationValue);

        this.trackerRepository.save(trackerdb);
    }
}
