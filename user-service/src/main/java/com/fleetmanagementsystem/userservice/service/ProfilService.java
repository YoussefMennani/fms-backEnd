package com.fleetmanagementsystem.userservice.service;

import com.fleetmanagementsystem.userservice.Model.Profile;
import com.fleetmanagementsystem.userservice.Model.ProfileRequest;
import com.fleetmanagementsystem.userservice.organization.Organization;
import com.fleetmanagementsystem.userservice.organization.OrganizationsRepository;
import com.fleetmanagementsystem.userservice.repository.OrganizationRepository;
import com.fleetmanagementsystem.userservice.repository.ProfilRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfilService {

    private final ProfilRepository profilRepository;
    private final OrganizationsRepository organizationsRepository;

    public Profile saveProfile(ProfileRequest profile) {
        Organization organization = organizationsRepository.findById(profile.getOrganization())
                .orElseThrow(()->new NotFoundException("Orgnization not found"));

        Profile profileData = Profile.builder()
                .menu(profile.getMenu())
                .name(profile.getName())
                .role(profile.getRole())
                .organization(organization)
                .description(profile.getDescription())
                .build();
            return profilRepository.save(profileData);
    }

    public List<Profile> getAllProfiles() {
        return profilRepository.findAll();
    }

    public String deleteProfile(String idProfile) {
        Optional<Profile> profile = profilRepository.findById(idProfile);
        if(profile.isPresent()){
            profilRepository.deleteById(idProfile);
            return profile.get().getId();
        }
        return null;
    }

    public List<Profile> findProfileByOrganizationId(String id){
        return this.profilRepository.findByOrganization(id);
    }
}
