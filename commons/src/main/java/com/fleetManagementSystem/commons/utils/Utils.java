package com.fleetManagementSystem.commons.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {

    public static LocalDateTime convertLongToLocalDateTime(long timestamp) {
        // Convert the timestamp (in milliseconds) to an Instant
        Instant instant = Instant.ofEpochMilli(timestamp);

        // Convert the Instant to LocalDateTime using the system's default time zone
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
