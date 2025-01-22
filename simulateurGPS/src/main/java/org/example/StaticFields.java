package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class StaticFields {


    public static OSRMResponse parseRouteData(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, OSRMResponse.class);
        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
            return null;
        }
    }

    public static String generateRandomIMEI() {
        Random random = new Random();
        StringBuilder imei = new StringBuilder();

        // Generate the first 14 digits
        for (int i = 0; i < 14; i++) {
            imei.append(random.nextInt(10));
        }

        // Calculate the 15th digit for checksum
        imei.append(calculateChecksum(imei.toString()));
        return imei.toString();
    }

    private static int calculateChecksum(String imei) {
        int sum = 0;
        for (int i = 0; i < imei.length(); i++) {
            int digit = Character.getNumericValue(imei.charAt(i));
            if (i % 2 == 1) digit *= 2;
            sum += digit > 9 ? digit - 9 : digit;
        }
        return (10 - (sum % 10)) % 10;
    }



}
