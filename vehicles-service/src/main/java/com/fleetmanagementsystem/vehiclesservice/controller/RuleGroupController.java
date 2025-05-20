package com.fleetmanagementsystem.vehiclesservice.controller;

import com.fleetManagementSystem.commons.vehicle.dto.AssignVLRulesRequest;
import com.fleetManagementSystem.commons.vehicle.model.RuleGroup;
import com.fleetManagementSystem.commons.vehicle.model.RuleGroupRequest;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.vehiclesservice.service.RuleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/vehicles/rule-groups")
public class RuleGroupController {

    @Autowired
    private RuleGroupService ruleGroupService;

    @GetMapping
    public List<RuleGroup> getAllRuleGroups() {
        return ruleGroupService.getAllRuleGroups();
    }

    @GetMapping("/byUserName")
    public List<RuleGroup> getAllRuleGroupsByUsername(Principal  principal) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        Jwt jwt = (Jwt) jwtAuthenticationToken.getCredentials(); // Use getCredentials() to access the JWT
        Map<String, Object> claims = jwt.getClaims();
        String username = claims.get("preferred_username").toString();

        return ruleGroupService.getAllRuleGroupsByUsername(username);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RuleGroup> getRuleGroupById(@PathVariable String id) {
        Optional<RuleGroup> ruleGroup = ruleGroupService.getRuleGroupById(id);
        return ruleGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public RuleGroup createRuleGroup(@RequestBody RuleGroupRequest ruleGroup, Principal principal) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        Jwt jwt = (Jwt) jwtAuthenticationToken.getCredentials(); // Use getCredentials() to access the JWT
        Map<String, Object> claims = jwt.getClaims();
        String username = claims.get("preferred_username").toString();
        ruleGroup.setUsername(username);

        return ruleGroupService.createRuleGroup(ruleGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuleGroup> updateRuleGroup(@PathVariable String id, @RequestBody RuleGroup updatedRuleGroup) {
        RuleGroup updated = ruleGroupService.updateRuleGroup(id, updatedRuleGroup);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRuleGroup(@PathVariable String id) {
        ruleGroupService.deleteRuleGroup(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/assignRuleGroupeToVl")
    public ResponseEntity<Vehicle> assignRuleGroupToVl(@RequestBody AssignVLRulesRequest assignVLRulesRequest) {
        return ruleGroupService.assignRuleGroupeToVl(assignVLRulesRequest);

    }

}
