package com.fleetmanagementsystem.reellistnerservice.controller;

import com.fleetmanagementsystem.reellistnerservice.service.TrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")  // Allow all origins
@RequiredArgsConstructor
public class ListnerController {

    private final TrackerService trackerService;


    @PostMapping("")
    public void receiveData(@RequestBody String body) {
        System.out.println("Received data: " + body);  // Log the incoming request body
        System.out.println("Data received");
        this.trackerService.handlePosition(body);

    }

    @ExceptionHandler
    public String handleMethodNotAllowed() {
        return "Method Not Allowed";
    }

}
