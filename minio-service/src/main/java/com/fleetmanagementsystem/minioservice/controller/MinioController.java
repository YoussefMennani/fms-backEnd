package com.fleetmanagementsystem.minioservice.controller;

import com.fleetmanagementsystem.minioservice.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "*") // Enable CORS for this controller
@RequestMapping ("/minio")
public class MinioController {
    @Autowired
    private MinioService minioService;

    @GetMapping("/generate-presigned-url/{folder}/{filename}")
    public String generatePresignedUrl(@PathVariable String folder, @PathVariable String filename) throws Exception {
        // Combine folder and filename to simulate folder structure
        String objectName = folder + "/" + filename;
        return minioService.getPreSignedUrl(objectName);
    }


    @GetMapping("/retrieve-presigned-url/{folder}/{filename}")
    public String retrievePresignedUrl(@PathVariable String folder, @PathVariable String filename) throws Exception {
        // Combine folder and filename to simulate folder structure
        String objectName = folder + "/" + filename;
        return minioService.getPreSignedGetUrl(objectName);
    }


}
