package com.fleetmanagementsystem.userservice.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "organizations") // Specify the MongoDB collection name
@Getter
@Setter
public class Organization {

    @Id
    private String id; // MongoDB's primary key
    private String name; // Top-level field for menu name
    private String description; // Top-level field for menu name
    private List<Data> data; // Nested data

    // Getters and Setters

    @Getter
    @Setter
    public static class Data {
        private Double id;
        private String header;
        private List<Item> items;

        // Getters and Setters
    }

    @Getter
    @Setter
    public static class Item {
        private Double id;
        private String text;
        private List<SubUnit> submenu;

        // Getters and Setters
    }

    @Getter
    @Setter
    public static class SubUnit {
        private Double id;
        private String text;
        private List<SubUnit> submenu;
        // Getters and Setters
    }
}
