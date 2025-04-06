package com.foodapp.controllers;

import com.foodapp.dto.response.ApiResponse;
import com.foodapp.services.Impl.FileStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUploadController {

    FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> upload(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.storeFile(file);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(ApiResponse.Status.SUCCESS)
                .data(fileUrl)
                .build());
    }
}
