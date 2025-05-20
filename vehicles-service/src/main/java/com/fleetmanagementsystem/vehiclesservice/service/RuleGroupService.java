package com.fleetmanagementsystem.vehiclesservice.service;

import com.fleetManagementSystem.commons.user.UserMinimal;
import com.fleetManagementSystem.commons.vehicle.dto.AssignVLRulesRequest;
import com.fleetManagementSystem.commons.vehicle.model.RuleGroup;
import com.fleetManagementSystem.commons.vehicle.model.RuleGroupRequest;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.vehiclesservice.FeignClient.OrganizationClient;
import com.fleetmanagementsystem.vehiclesservice.exception.VehicleEntityNotFoundException;
import com.fleetmanagementsystem.vehiclesservice.repository.RuleGroupRepository;
import com.fleetmanagementsystem.vehiclesservice.repository.VehicleRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RuleGroupService {

    @Autowired
    private RuleGroupRepository ruleGroupRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private OrganizationClient organizationClient;

    public List<RuleGroup> getAllRuleGroups() {
        return ruleGroupRepository.findAll();
    }

    public Optional<RuleGroup> getRuleGroupById(String id) {
        return ruleGroupRepository.findById(id);
    }

    public RuleGroup createRuleGroup(RuleGroupRequest ruleGroup) {
        List<String> users = ruleGroup.getUsers();

        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();

        List<UserMinimal> minimalList = organizationClient
                .getUsersFromList(token, users)
                .orElseThrow(() -> new NotFoundException("Users not found"));

        RuleGroup   ruleGroupBuilt = RuleGroup.builder()
                .id(ruleGroup.getId())
                .ruleGroupName(ruleGroup.getRuleGroupName())
                .username(ruleGroup.getUsername())
                .rules(ruleGroup.getRules())
                .users(minimalList)
                .build();

        return ruleGroupRepository.save(ruleGroupBuilt);
    }

    public RuleGroup updateRuleGroup(String id, RuleGroup updatedRuleGroup) {
        if (ruleGroupRepository.existsById(id)) {
            updatedRuleGroup.setId(id);
            return ruleGroupRepository.save(updatedRuleGroup);
        }
        return null;
    }

    public void deleteRuleGroup(String id) {
        ruleGroupRepository.deleteById(id);
    }

    public List<RuleGroup> getAllRuleGroupsByUsername(String username) {
        Optional<List<RuleGroup>>ruleGroup =  ruleGroupRepository.getRuleGroupByUsername(username);

         return ruleGroup.get();
    }

    public ResponseEntity<Vehicle> assignRuleGroupeToVl(AssignVLRulesRequest assignVLRulesRequest) {
        Vehicle vehicle = vehicleRepository.findById(assignVLRulesRequest.getVehicleId())
                .orElseThrow(() -> new VehicleEntityNotFoundException(
                        String.format("No Vehicle found with the provided ID: %s", assignVLRulesRequest.getVehicleId())
                ));

        List<RuleGroup> ruleGroups = ruleGroupRepository.findAllById(assignVLRulesRequest.getRuleGroupIds());
        vehicle.setRuleGroupList(ruleGroups);

        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }

}
