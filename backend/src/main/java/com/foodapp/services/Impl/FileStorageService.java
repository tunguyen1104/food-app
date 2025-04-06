package com.foodapp.services.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads";

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty or null");
        }

        try {
            String originalFileName = Path.of(file.getOriginalFilename()).getFileName().toString();
            String fileName = UUID.randomUUID() + "_" + originalFileName;

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("File saved to: {}", targetLocation.toAbsolutePath());

            return "/uploads/" + fileName;
        } catch (IOException ex) {
            log.error("Failed to store file", ex);
            throw new RuntimeException("Could not store file: " + ex.getMessage(), ex);
        }
    }
}
