package com.mdn.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/images")
@Slf4j
public class ImageController {

    @Value("${local.storage.path}")
    private String storagePath;

    @GetMapping("/{folderName}/{entityId}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String folderName,
                                             @PathVariable String entityId,
                                             @PathVariable String imageName) {
        log.info("Getting image");
        try {
            // Construct the file path
            Path imagePath = Paths.get(storagePath, folderName, entityId, imageName);

            // Load the image as a resource
            Resource resource = new UrlResource(imagePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Could not read the image file: " + imageName);
            }

            // Return the image file as an attachment or inline
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new RuntimeException("Error while serving image: " + ex.getMessage(), ex);
        }
    }
}
