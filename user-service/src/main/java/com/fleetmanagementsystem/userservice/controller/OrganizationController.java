package com.fleetmanagementsystem.userservice.controller;


import com.fleetmanagementsystem.userservice.Model.Menu;
import com.fleetmanagementsystem.userservice.Model.Organization;
import com.fleetmanagementsystem.userservice.Model.Profile;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import com.fleetmanagementsystem.userservice.controller.dto.UserExtraRequest;
import com.fleetmanagementsystem.userservice.exception.UserExtraNotFoundException;
import com.fleetmanagementsystem.userservice.service.MenuService;
import com.fleetmanagementsystem.userservice.service.OrganizationService;
import com.fleetmanagementsystem.userservice.service.ProfilService;
import com.fleetmanagementsystem.userservice.service.UserExtraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping("")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        Organization org =  organizationService.saveOrganization(organization);
        return ResponseEntity.ok(org);
    }

        @GetMapping("")
    public ResponseEntity<List<Organization>> getOrganization(){
        List<Organization> organizationList = this.organizationService.getAllOrganizations();
        return  ResponseEntity.ok(organizationList);
    }


        @GetMapping("/{idOrganization}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable String idOrganization){
        Organization org = this.organizationService.getAllOrganizationById(idOrganization);
        return  ResponseEntity.ok(org);
    }

    @DeleteMapping("/{idOrganization}")
    public ResponseEntity<String> deleteOrganizationById(@PathVariable String idOrganization){
        String org = this.organizationService.deleteAllOrganizationById(idOrganization);
        return  ResponseEntity.ok(org);
    }

    @PostMapping("/updateOrganization")
    public ResponseEntity<Organization> updateOrganization(@RequestBody Organization organization) {
        Organization responseMenu =  this.organizationService.updateOrganization(organization);
        return ResponseEntity.ok(responseMenu);
    }





//
//    private final OrganizationService organizationService;
//
//    @PostMapping("")
//    public ResponseEntity<Organization> saveOrganization(@RequestBody Organization organization){
//        Organization responseOrganization = this.organizationService.saveOrganization(organization);
//        return ResponseEntity.ok(responseOrganization);
//    }
//
//    @GetMapping("")
//    public ResponseEntity<List<Organization>> getOrganization(){
//        List<Organization> organizationList = this.organizationService.getAllOrganizations();
//        return  ResponseEntity.ok(organizationList);
//    }
//
//    @GetMapping("/{idOrganization}")
//    public ResponseEntity<Organization> getOrganizationById(@PathVariable String idOrganization){
//        Organization org = this.organizationService.getAllOrganizationById(idOrganization);
//        return  ResponseEntity.ok(org);
//    }
//
//    @DeleteMapping("/{idOrganization}")
//    public ResponseEntity<String> deleteOrganization(@PathVariable String idOrganization){
//        String idOrg = this.organizationService.deleteOrganization(idOrganization);
//        return  ResponseEntity.ok(idOrg);
//    }

}
