package com.mdn.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AmazonS3StorageService {

    private final AmazonS3 amazonS3Client;
    private static final String BUCKET_NAME = "coffee-and-happiness-images";

    public AmazonS3StorageService() {
        this.amazonS3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion("eu-central-1")
                .build();
    }

    public String saveImage(MultipartFile image, String target, Integer entityId) {
        try {
            String key = generateImageKey(target, entityId, image.getOriginalFilename());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, key, image.getInputStream(), metadata);
            amazonS3Client.putObject(request);

            return amazonS3Client.getUrl(BUCKET_NAME, key).toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while saving image: " + e.getMessage(), e);
        }
    }

    private String generateImageKey(String target, Integer entityId, String originalFilename) {
        return target + "/" + entityId + "/" + originalFilename;
    }

    public void deleteImage(String target, Integer id) {
        String prefix = target + "/" + id + "/";

        ObjectListing objectListing = amazonS3Client.listObjects(BUCKET_NAME, prefix);

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            amazonS3Client.deleteObject(BUCKET_NAME, objectSummary.getKey());
        }
    }
}
