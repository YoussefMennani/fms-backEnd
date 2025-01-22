package com.fleetmanagementsystem.userservice.service;

import com.fleetmanagementsystem.userservice.Model.Menu;
import com.fleetmanagementsystem.userservice.Model.Organization;
import com.fleetmanagementsystem.userservice.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationService {


        private  final OrganizationRepository organizationRepository;

    /**
     * Save or update a menu by its menuName.
     */
    public Organization saveOrganization(Organization organization) {
        Organization org = organizationRepository.save(organization);
        return org;
    }

//    /**
//     * Get a menu by its ID.
//     */
//    public Menu getMenuById(String id) {
//        return organizationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Menu not found with ID: " + id));
//    }
//
//    /**
//     * Get a menu by its menuName.
//     */
//    public Menu getMenuByMenuName(String menuName) {
//        return organizationRepository.findByMenuName(menuName)
//                .orElseThrow(() -> new RuntimeException("Menu not found with name: " + menuName));
//    }



//    private  final OrganizationRepository organizationRepository;
//    public Organization saveOrganization(Organization organization) {
//        Organization org = this.organizationRepository.save(organization);
//        return  org;
//    }
//
    public List<Organization> getAllOrganizations() {
        return this.organizationRepository.findAll();
    }
//
    public Organization getAllOrganizationById(String idOrganization) {
        Optional<Organization> org = this.organizationRepository.findById(idOrganization);
        if(org.isPresent()){
            return org.get();
        }else{
            return null;
        }
    }


        public Organization updateOrganization(Organization organization) {
            Optional<Organization> existingOrgOptional = organizationRepository.findById(organization.getId());

            if (existingOrgOptional.isPresent()) {
                Organization existingOrg = existingOrgOptional.get();
                existingOrg.setData(organization.getData());
                return organizationRepository.save(existingOrg);
            } else {
                return null;
            }
        }

    public String deleteAllOrganizationById(String idOrganization) {
        organizationRepository.deleteById(idOrganization);
        return idOrganization;
    }
//
//    public String deleteOrganization(String idOrganization) {
//        Optional<Organization> org = this.organizationRepository.findById(idOrganization);
//        if (org.isPresent()){
//            this.organizationRepository.deleteById(idOrganization);
//            return org.get().getId();
//        }else{
//            return  null;
//        }
//    }
}
