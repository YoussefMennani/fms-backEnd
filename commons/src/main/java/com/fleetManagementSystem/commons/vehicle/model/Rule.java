package com.fleetManagementSystem.commons.vehicle.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Rule {
    private Long id;
    private String factor;
    private String operator;
    private String value;
    private List<String> actions;

    //        @DBRef(lazy = true)
    private String polygon;
}