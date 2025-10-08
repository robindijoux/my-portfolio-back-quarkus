package com.rbndjx.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@ApplicationScoped
public class S3Service {

    @Inject
    S3Client s3Client;

    @Inject
    S3Presigner s3Presigner;

    @ConfigProperty(name = "aws.s3.bucket-name")
    String bucketName;

    public String uploadFile(InputStream fileStream, String fileName, String mimeType, String folder, long fileSize) {
        String key = folder != null ? folder + "/" + fileName : fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(mimeType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileStream, fileSize));

        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }

    public void deleteFile(String fileKey) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public void deleteFileByUrl(String url) {
        // Extract key from URL
        String key = extractKeyFromUrl(url);
        deleteFile(key);
    }

    public String generateSignedUrl(String fileName, String folder, int expirationSeconds) {
        String key = folder != null ? folder + "/" + fileName : fileName;

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expirationSeconds))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private String extractKeyFromUrl(String url) {
        // Extract key from S3 URL
        // Example: https://bucket-name.s3.amazonaws.com/folder/file.jpg -> folder/file.jpg
        String[] parts = url.split(bucketName + ".s3.amazonaws.com/");
        if (parts.length > 1) {
            return parts[1];
        }
        // Alternative format: https://s3.region.amazonaws.com/bucket/key
        parts = url.split(bucketName + "/");
        if (parts.length > 1) {
            return parts[1];
        }
        throw new IllegalArgumentException("Invalid S3 URL format: " + url);
    }

    public String generateUniqueFileName(String originalName) {
        long timestamp = System.currentTimeMillis();
        String random = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalName);
        return timestamp + "-" + random + extension;
    }

    public String getFolderByMimeType(String mimeType) {
        if (mimeType.startsWith("image/")) {
            return "images";
        } else if (mimeType.startsWith("video/")) {
            return "videos";
        } else if (mimeType.equals("application/pdf") || 
                   mimeType.contains("document") || 
                   mimeType.equals("text/plain")) {
            return "documents";
        }
        return "others";
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex != -1 ? fileName.substring(lastDotIndex) : "";
    }
}
