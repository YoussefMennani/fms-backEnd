package com.fleetmanagementsystem.userservice.organization;


import com.fleetmanagementsystem.userservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationsController {

    @Autowired
    private OrganizationsService organizationsService;

    @Autowired
    private RoleService roleService;
    @PostMapping
    public Organization createOrganization(@RequestBody Organization organization) {
        return organizationsService.createOrganization(organization);
    }

    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationsService.getAllOrganizations();
    }

    @GetMapping("/{id}")
    public Organization getOrganizationById(@PathVariable String id) {
        return organizationsService.getOrganizationById(id);
    }

    @PutMapping("/{id}")
    public Organization updateOrganization(@PathVariable String id, @RequestBody Organization organizationDetails) {
        return organizationsService.updateOrganization(id, organizationDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteOrganization(@PathVariable String id) {
        organizationsService.deleteOrganization(id);
    }

    @GetMapping("/tree/{rootId}")
    public OrganizationTreeDTO getOrganizationTree(@PathVariable String rootId) {

        return organizationsService.buildOrganizationTree(rootId);
    }

    @GetMapping("/roots")
    public List<Organization> getRootOrganizations(Principal principal) {

        boolean isAuthorized =  roleService.isAuthorized(principal,List.of("ROLE_SUPER_ADMIN"));
        List<Organization> organizationList;
        if(isAuthorized){
            organizationList = organizationsService.getRootOrganizations();
        }else{
//            organizationList = List.of(this.organizationsService.getOrganizationById(roleService.extractIdOrganization(principal)));
            Organization org = this.organizationsService.getOrganizationById(roleService.extractIdOrganization(principal));
            organizationList = this.organizationsService.destructuredOrganizations(org);
        }
        return  organizationList;

    }
}