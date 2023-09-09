package com.mdn.backend.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AzureBlobStorageService {

    @Value("${azure.storage.connection-string}")
    private String azureConnectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public String saveImage(MultipartFile image, String folderName, Integer entityId) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(azureConnectionString).buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            String imageName = generateUniqueImageName(Objects.requireNonNull(image.getOriginalFilename()));

            BlobClient blobClient = containerClient.getBlobClient(folderName + "/" + entityId + "/" + imageName);

            byte[] imageBytes = image.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

            blobClient.upload(inputStream, imageBytes.length, true);

            return blobClient.getBlobUrl();
        } catch (IOException ex) {
            throw new RuntimeException("Error while saving image to Azure Blob Storage: " + ex.getMessage(), ex);
        }
    }

    public void deleteImage(String folderName, Integer entityId) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(azureConnectionString).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        String prefix = folderName + "/" + entityId + "/";

        containerClient.listBlobsByHierarchy(prefix).forEach(blobItem -> {
            BlobClient blobClient = containerClient.getBlobClient(blobItem.getName());
            blobClient.delete();
        });
    }

    private String generateUniqueImageName(String originalFileName) {
        String[] parts = originalFileName.split("\\.");
        String fileExtension = parts[parts.length - 1];
        String uniqueName = UUID.randomUUID().toString();

        return uniqueName + "." + fileExtension;
    }
}
