package com.fleetmanagementsystem.minioservice.service;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.time.Duration;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.GetPresignedObjectUrlArgs;


@Service
public class MinioService {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String getPreSignedUrl(String objectName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        // Create a GetPresignedObjectUrlArgs object
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)         // HTTP method (PUT for uploading)
                .bucket(bucketName)         // Bucket name
                .object(objectName)         // Object name (file name)
                .expiry(300)                // URL expiration time in seconds (5 minutes)
                .build();

        // Generate a pre-signed URL
        return minioClient.getPresignedObjectUrl(args);
    }


    // Generate Pre-Signed URL for retrieving images
    public String getPreSignedGetUrl(String objectName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)         // HTTP method (GET for retrieving)
                .bucket(bucketName)         // Bucket name
                .object(objectName)         // Object name (file name)
                .expiry(300)                // URL expiration time in seconds (5 minutes)
                .build();

        return minioClient.getPresignedObjectUrl(args);
    }

}
