package com.fleetManagementSystem.commons.subscription.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDetails {
    private String number;
    private String expMonth;
    private String expYear;
    private String cvc;

}