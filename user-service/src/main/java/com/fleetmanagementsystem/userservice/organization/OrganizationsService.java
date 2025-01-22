package com.fleetmanagementsystem.userservice.organization;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationsService {

    @Autowired
    private OrganizationsRepository organizationsRepository;

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

    List<Organization> destructuredOrganizations(Organization organization){
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

}