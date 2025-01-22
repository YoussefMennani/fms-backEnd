package com.fleetManagementSystem.commons.groupVehicleNotifications;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "group_vl_notification")
@Setter
@Getter
public class GroupVlNotification {
    @Id
    private String id;
    private String name;
    private List<String> vehicles; // List of vehicle IDs
    private List<String> users;    // List of user IDs to notify
    private Date createdAt;
    private Date updatedAt;

}