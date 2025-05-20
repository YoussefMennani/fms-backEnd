package com.fleetmanagementsystem.subscriptionservice.controller;

import com.fleetManagementSystem.commons.subscription.dto.SubscriptionResponse;
import com.fleetManagementSystem.commons.subscription.model.Plan;
import com.fleetManagementSystem.commons.subscription.model.Subscription;
import com.fleetmanagementsystem.subscriptionservice.service.PlanService;
import com.fleetmanagementsystem.subscriptionservice.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable String id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @PostMapping("/plans")
    public ResponseEntity<Plan> createPlan(@Valid @RequestBody Plan plan) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.createPlan(plan));
    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable String id, @Valid @RequestBody Plan planDetails) {
        return ResponseEntity.ok(planService.updatePlan(id, planDetails));
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable String id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }


    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/getAllSubscriptionsByOrganization")
    public List<SubscriptionResponse> getAllSubscriptionsByOrganization(@RequestParam String organizationId) {
        return subscriptionService.getAllSubscriptionsByOrganization(organizationId);
    }


    //    @GetMapping("/plans/{id}")
    //    public ResponseEntity<Plan> getSubscriptionsById(@PathVariable String id) {
    //        return ResponseEntity.ok(planService.getPlanById(id));
    //    }
    //
    @PostMapping("/subscriptions")
    public ResponseEntity<Subscription> createSubscriptions(@Valid @RequestBody Subscription subscription) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.createSubscription(subscription));
    }

    @PutMapping("/subscriptions/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable String id, @RequestBody Subscription subscriptionData) {
        Subscription updatedSubscription = subscriptionService.updateSubscription(id, subscriptionData);
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<Void> deleteSubscriptions(@PathVariable String id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/subscriptions/{id}/enable")
    public ResponseEntity<Subscription> enableSubscription(@PathVariable("id") String id) {
        Subscription enabledSubscription = subscriptionService.enableSubscription(id);
        return new ResponseEntity<>(enabledSubscription, HttpStatus.OK);
    }

    @PutMapping("/subscriptions/{id}/disable")
    public ResponseEntity<Subscription> disableSubscription(@PathVariable("id") String id) {
        Subscription disabledSubscription = subscriptionService.disableSubscription(id);
        return new ResponseEntity<>(disabledSubscription, HttpStatus.OK);
    }

    @GetMapping("/isOrganizationAuthorized")
    public boolean isOrganizationAuthorized(@RequestParam String organizationId) {
        return subscriptionService.isOrganizationAuthorized(organizationId);
    }



}