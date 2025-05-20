package com.fleetmanagementsystem.subscriptionservice.service;

import com.fleetManagementSystem.commons.exception.PlanNotFoundException;
import com.fleetManagementSystem.commons.subscription.model.Plan;
import com.fleetmanagementsystem.subscriptionservice.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Plan getPlanById(String id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found with id: " + id));
    }

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public Plan updatePlan(String id, Plan planDetails) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found with id: " + id));

        plan.setName(planDetails.getName());
        plan.setPrice(planDetails.getPrice());
        plan.setVehicles(planDetails.getVehicles());
        plan.setGpsTrackers(planDetails.getGpsTrackers());
        plan.setUsers(planDetails.getUsers());

        return planRepository.save(plan);
    }

    public void deletePlan(String id) {
        if (!planRepository.existsById(id)) {
            throw new PlanNotFoundException("Plan not found with id: " + id);
        }
        planRepository.deleteById(id);
    }
}