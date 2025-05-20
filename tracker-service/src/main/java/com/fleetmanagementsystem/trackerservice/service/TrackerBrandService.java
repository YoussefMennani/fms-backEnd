package com.fleetmanagementsystem.trackerservice.service;

import com.fleetManagementSystem.commons.tracker.dto.TrackerBrandRequest;
import com.fleetManagementSystem.commons.tracker.dto.TrackerBrandResponse;
import com.fleetmanagementsystem.trackerservice.exception.TrackerEntityNotFoundException;
import com.fleetmanagementsystem.trackerservice.mapper.TrackerBrandMapper;
import com.fleetManagementSystem.commons.tracker.model.TrackerBrand;
import com.fleetmanagementsystem.trackerservice.repository.TrackerBrandRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TrackerBrandService {

    private final TrackerBrandRepository trackerBrandRepository;
    private final TrackerBrandMapper trackerBrandMapper;
    public TrackerBrandResponse createTrackerBrand(TrackerBrandRequest trackerBrandRequest) {
        var trackerBrand = this.trackerBrandRepository.save(trackerBrandMapper.toTrackerBrand(trackerBrandRequest));
        return trackerBrandMapper.fromTrackerBrand(trackerBrand);
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

    public TrackerBrandResponse putTrackerBrandById(String brandId, TrackerBrandRequest newTrackerBrand) {

        var brand =  this.trackerBrandRepository.findById(brandId)
                .orElseThrow(()-> new TrackerEntityNotFoundException(
                        String.format("No Tracker found with the provided ID: %s", brandId)
                ));

        if (brand.isEquivalentTo(newTrackerBrand)){
            System.out.println(" No changes brand for both old Model and new Model brand !");

        }else{
            mergeTrackerBrand(brand,newTrackerBrand);
            this.trackerBrandRepository.save(brand);
        }

        return this.trackerBrandMapper.fromTrackerBrand(brand);
    }


    public void mergeTrackerBrand(TrackerBrand brand, TrackerBrandRequest trackerBrandRequest){

        Optional.ofNullable(trackerBrandRequest.brandName())
                .filter(StringUtils::isNotBlank)
                .ifPresent(brand::setBrandName);

        Optional.ofNullable(trackerBrandRequest.originCountry())
                .filter(StringUtils::isNotBlank)
                .ifPresent(brand::setOriginCountry);

    }

    public boolean deleteBrandById(String brandId) {
       if( this.trackerBrandRepository.existsById(brandId) ){
           this.trackerBrandRepository.deleteById(brandId);
           return true;
       } else {
        throw new TrackerEntityNotFoundException("Brand not found with ID: " + brandId);
    }
    }
}
