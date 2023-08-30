package com.mdn.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AmazonS3StorageService {

    private final AmazonS3 amazonS3Client;

    public AmazonS3StorageService() {
        this.amazonS3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion("eu-central-1")
                .build();
    }

    public String saveImage(MultipartFile image, String target, Integer entityId) {
        try {
            String bucketName = "coffee-and-happiness-images";
            String key = generateImageKey(target, entityId, image.getOriginalFilename());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            PutObjectRequest request = new PutObjectRequest(bucketName, key, image.getInputStream(), metadata);
            amazonS3Client.putObject(request);

            return amazonS3Client.getUrl(bucketName, key).toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while saving image: " + e.getMessage(), e);
        }
    }

    private String generateImageKey(String target, Integer entityId, String originalFilename) {
        return target + "/" + entityId + "/" + originalFilename;
    }
}
