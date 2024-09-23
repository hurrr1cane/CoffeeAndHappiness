package com.mdn.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalStorageService {

    @Value("${local.storage.path}")
    private String storagePath;

    @Value("${local.storage.urlPrefix}")
    private String storageUrlPrefix; // Base URL prefix (e.g., http://localhost:8080/images)


    public String saveImage(MultipartFile image, String folderName, Integer entityId) {
        try {
            String imageName = generateUniqueImageName(Objects.requireNonNull(image.getOriginalFilename()));
            String directoryPath = storagePath + File.separator + folderName + File.separator + entityId;
            File directory = new File(directoryPath);

            if (!directory.exists()) {
                directory.mkdirs();  // Create directory if it doesn't exist
            }

            String filePath = directoryPath + File.separator + imageName;
            File imageFile = new File(filePath);

            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(image.getBytes());
            }

            // Return the relative URL instead of the local path
            return storageUrlPrefix + "/" + folderName + "/" + entityId + "/" + imageName;
        } catch (IOException ex) {
            throw new RuntimeException("Error while saving image locally: " + ex.getMessage(), ex);
        }
    }

    public void deleteImage(String folderName, Integer entityId) {
        try {
            String directoryPath = storagePath + File.separator + folderName + File.separator + entityId;
            Path directory = Paths.get(directoryPath);

            if (Files.exists(directory)) {
                Files.walk(directory)
                        .map(Path::toFile)
                        .forEach(File::delete);  // Delete files recursively
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error while deleting image: " + ex.getMessage(), ex);
        }
    }

    private String generateUniqueImageName(String originalFileName) {
        String[] parts = originalFileName.split("\\.");
        String fileExtension = parts[parts.length - 1];
        String uniqueName = UUID.randomUUID().toString();

        return uniqueName + "." + fileExtension;
    }
}
