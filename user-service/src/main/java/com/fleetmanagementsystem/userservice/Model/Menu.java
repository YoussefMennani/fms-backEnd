package com.fleetmanagementsystem.userservice.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "menus") // Specify the MongoDB collection name
@Getter
@Setter
public class Menu {

    @Id
    private String id; // MongoDB's primary key
    private String menuName; // Top-level field for menu name
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
        private String icon;
        private boolean available;
        private String link;
        private List<SubMenu> submenu;

        // Getters and Setters
    }

    @Getter
    @Setter
    public static class SubMenu {
        private Double id;
        private String text;
        private boolean available;
        private String icon;
        private String link;
        private List<SubMenu> submenu;
        // Getters and Setters
    }
}
