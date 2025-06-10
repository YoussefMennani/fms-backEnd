//package com.fleetmanagementsystem.subscriptionservice.service;
//
//import com.fleetManagementSystem.commons.subscription.enums.PaymentStatus;
//import com.fleetManagementSystem.commons.subscription.model.Payment;
//import com.fleetmanagementsystem.subscriptionservice.repository.PaymentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//@Service
//public class PaymentService {
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private StripeApiPaymentGateway stripeApiPaymentGateway;
//
//    @Transactional
//    public Payment makePayment(Payment payment){
//        payment.setStatus(PaymentStatus.PENDING);
//        Payment paymentStripe = this.stripeApiPaymentGateway.processPayment(payment);
//        if(paymentStripe.getStatus().equals(PaymentStatus.CONFIRMED)){
//            payment.setStatus(PaymentStatus.APPROVED);
//            payment.setPaymentDate(LocalDateTime.now());
//        }else{
//            payment.setStatus(PaymentStatus.FAILED);
//            payment.setPaymentDate(LocalDateTime.now());
//        }
//
//        this.paymentRepository.save(payment);
//        return payment;
//    }
//
//}
