package com.fleetmanagementsystem.userservice.organization;


import com.fleetManagementSystem.commons.driver.model.DriverResponse;
import com.fleetManagementSystem.commons.mapper.CustomResponse;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleResponse;
import com.fleetmanagementsystem.userservice.FeignClient.DriversClient;
import com.fleetmanagementsystem.userservice.FeignClient.VehiclesClient;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import com.fleetmanagementsystem.userservice.service.UserExtraServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationsService {

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Autowired
    private UserExtraServiceImpl userExtraService;
    @Autowired
    private VehiclesClient vehiclesClient;
    @Autowired
    private DriversClient driversClient;

    // CRUD methods (unchanged)
    public Organization createOrganization(Organization organization) {
        return organizationsRepository.save(organization);
    }

    public List<Organization> getAllOrganizations() {
        return organizationsRepository.findAll();
    }

    public Organization getOrganizationById(String id) {
        return organizationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

    }

    public List<Organization> destructuredOrganizations(Organization organization){
        OrganizationTreeDTO orgs = this.buildOrganizationTree(organization.getId());
        if(orgs != null){
            List<Organization> listOrgs = new ArrayList<>();
            this.destructOrganization(orgs,listOrgs);
            return listOrgs;
        }else{
            return null;
        }
    }

    void destructOrganization(OrganizationTreeDTO orgs,List<Organization>  listOrgs){
        listOrgs.add(new Organization(orgs.getId(),orgs.getName(),null));
        if(orgs.getChildren().size() > 0){
            for (OrganizationTreeDTO organizationTreeDTO:orgs.getChildren()) {
                destructOrganization(organizationTreeDTO,listOrgs);
            }
        }
    }

    public Organization updateOrganization(String id, Organization organizationDetails) {
        Organization organization = organizationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        organization.setName(organizationDetails.getName());
        organization.setParentId(organizationDetails.getParentId());
        return organizationsRepository.save(organization);
    }

    public void deleteOrganization(String id) {
        organizationsRepository.deleteById(id);
    }

    // Tree-building method
    public OrganizationTreeDTO buildOrganizationTree(String rootId) {
        Organization root = organizationsRepository.findById(rootId)
                .orElseThrow(() -> new RuntimeException("Root organization not found"));
        return buildTree(root);
    }

    private OrganizationTreeDTO buildTree(Organization node) {
        OrganizationTreeDTO dto = new OrganizationTreeDTO();
        dto.setId(node.getId());
        dto.setName(node.getName());
        dto.setParentId(node.getParentId());

        // Find children and recursively build the tree
        List<Organization> children = organizationsRepository.findByParentId(node.getId());
        List<OrganizationTreeDTO> childDTOs = children.stream()
                .map(this::buildTree) // Recursively build the tree for each child
                .collect(Collectors.toList());

        dto.setChildren(childDTOs); // Set children in the DTO
        return dto;
    }

    public List<Organization> getRootOrganizations() {
        return organizationsRepository.findByParentIdIsNull();
    }



    // Build Chart OrgChart exemple of root 67897a15e0883f2e61976f91

    public OrgChartDataDTO buildOrgChartData(String rootId) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();


        List<VehicleResponse> vehicles = vehiclesClient.getAllVehiclesByOrganization(token, rootId).getBody().data();
        List<DriverResponse> drivers = driversClient.getAllDrivers(token).getBody();
        List<UserExtra> users = userExtraService.getAllUsers();
        Organization root = organizationsRepository.findById(rootId)
                .orElseThrow(() -> new RuntimeException("Root organization not found"));
        OrgChartDataDTO orgChartDataDTO = buildOrgChartTree(root,users,drivers,vehicles);return orgChartDataDTO;
    }

    private OrgChartDataDTO buildOrgChartTree(Organization node,List<UserExtra> users,List<DriverResponse> drivers ,List<VehicleResponse> vehicles) {
        OrgChartDataDTO dto = new OrgChartDataDTO();
        dto.setId(node.getId());
        dto.setName(node.getName());
        dto.setParentId(node.getParentId());
//        dto.setVisible(true);
        // Find children and recursively build the tree
        List<Organization> children = organizationsRepository.findByParentId(node.getId());
        List<OrgChartDataDTO> childDTOs = children.stream()
                .map(child -> buildOrgChartTree(child, users, drivers, vehicles)) // Pass all parameters explicitly
                .collect(Collectors.toList());

        dto.setUsers(users.stream().filter(user-> {
//            System.out.println(user.getOrganization());
            return user.getOrganization().getId().equals(node.getId()) ;
        }).toList());

        dto.setDrivers(drivers.stream().filter(driver-> {
//            System.out.println(driver.getOrganization());
            return driver.getOrganization().getId().equals(node.getId());
        }).toList());

        dto.setVehicles(vehicles.stream().filter(vehicle->vehicle.getOrganization().getId().equals(node.getId()) ).toList());

        dto.setChildren(childDTOs); // Set children in the DTO
        return dto;
    }

    public Organization getRootOrganizationById(String idOrg){
        Organization targetOrg = this.organizationsRepository.findById(idOrg).orElseThrow(() -> new RuntimeException("Root organization not found"));
        if(targetOrg.getParentId() == null){
            return targetOrg;
        }else {
            return getRootOrganizationById(targetOrg.getParentId());
        }
    }

    public List<Organization> getChildOrganization(String idOrg){
       Organization organization = organizationsRepository.findById(idOrg)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
       List<Organization> organizationList = destructuredOrganizations(organization);
       return organizationList;
    }
}