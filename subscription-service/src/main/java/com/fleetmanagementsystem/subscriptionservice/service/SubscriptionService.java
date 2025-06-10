package com.fleetmanagementsystem.subscriptionservice.service;

import com.fleetManagementSystem.commons.exception.PlanNotFoundException;
import com.fleetManagementSystem.commons.subscription.dto.SubscriptionResponse;
import com.fleetManagementSystem.commons.subscription.enums.PaymentStatus;
import com.fleetManagementSystem.commons.subscription.model.Payment;
import com.fleetManagementSystem.commons.subscription.model.Subscription;

import com.fleetmanagementsystem.subscriptionservice.repository.PaymentRepository;
import com.fleetmanagementsystem.subscriptionservice.repository.SubscriptionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {


    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }


//    public Plan getPlanById(String id) {
//        return planRepository.findById(id)
//                .orElseThrow(() -> new PlanNotFoundException("Plan not found with id: " + id));
//    }
//

    public List<SubscriptionResponse> getAllSubscriptionsByOrganization(String orgId) {
        List<Subscription> subscriptionsByOrganization = subscriptionRepository.findSubscriptionsByOrganization(orgId);
        List<SubscriptionResponse> subscriptionResponseList =  new ArrayList<>();
        for (Subscription subscription: subscriptionsByOrganization) {
            List<Payment> paymentList = paymentRepository.findPaymentsBySubscriptionId(subscription.getId());

            SubscriptionResponse subscriptionResponse = SubscriptionResponse.builder()
                    .id(subscription.getId())
                    .organization(subscription.getOrganization())
                    .plan(subscription.getPlan())
                    .paymentList(paymentList)
                    .startDate(subscription.getStartDate())
                    .endDate(subscription.getEndDate())
                    .discount(subscription.getDiscount())
                    .finalPrice(subscription.getFinalPrice())
                    .enabled(subscription.isEnabled())
                    .build();
            subscriptionResponseList.add(subscriptionResponse);

        }
        return subscriptionResponseList;
    }

    public Subscription createSubscription(Subscription subscription) {

        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(String id, Subscription subscriptionData) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);
        if (optionalSubscription.isPresent()) {
            Subscription existingSubscription = optionalSubscription.get();

            // Update the fields of the existing subscription with the new data
            existingSubscription.setOrganization(subscriptionData.getOrganization());
            existingSubscription.setPlan(subscriptionData.getPlan());
            existingSubscription.setStartDate(subscriptionData.getStartDate());
            existingSubscription.setEndDate(subscriptionData.getEndDate());
            existingSubscription.setDiscount(subscriptionData.getDiscount());
            existingSubscription.setFinalPrice(subscriptionData.getFinalPrice());

            // Save the updated subscription
            return subscriptionRepository.save(existingSubscription);
        } else {
            throw new PlanNotFoundException("Subscription not found with id: " + id);        }
    }

    public void deleteSubscription(String id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new PlanNotFoundException("Subscription not found with id: " + id);
        }
        subscriptionRepository.deleteById(id);
    }

    public Subscription enableSubscription(String id) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            subscription.setEnabled(true); // Enable the subscription
            return subscriptionRepository.save(subscription);
        } else {
            throw new RuntimeException("Subscription not found with id: " + id);
        }
    }

    public Subscription disableSubscription(String id) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(id);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            subscription.setEnabled(false); // Disable the subscription
            return subscriptionRepository.save(subscription);
        } else {
            throw new RuntimeException("Subscription not found with id: " + id);
        }
    }

    public boolean isOrganizationAuthorized(String organizationId) {
        // Fetch the subscription by organization ID
//        Subscription subscription = subscriptionRepository.findByOrganizationId(organizationId);

        Date now = new Date();
        Subscription subscription =  subscriptionRepository.findActiveSubscription(organizationId, now);
        if (subscription == null) {
            return false; // No subscription found for the organization
        }

        List<Payment> paymentDetails = paymentRepository.findPaymentsBySubscriptionId(subscription.getId());


        // Check if the subscription is enabled and the current date is within the start and end date
        return subscription.isEnabled() ||  (paymentDetails.size() >0 && paymentDetails.get(0).getStatus() == PaymentStatus.CONFIRMED);
    }

}