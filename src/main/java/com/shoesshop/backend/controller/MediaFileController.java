package com.shoesshop.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shoesshop.backend.dto.MediaResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
public class MediaFileController {

    @Autowired
    private Environment environment;

    @PostMapping("/uploadImage")
    public ResponseEntity<MediaResponse> uploadImage(@RequestParam("image")MultipartFile image) {
        log.info(environment.getProperty("my-secret-key"));
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", environment.getProperty("cloud-dinary.cloud-name"));
        config.put("api_key", environment.getProperty("cloud-dinary.api-key"));
        config.put("api_secret", environment.getProperty("cloud-dinary.api-secret"));
        Cloudinary cloudinary = new Cloudinary(config);
        try {
            String fileNameWithExtension = image.getOriginalFilename();
            if (fileNameWithExtension != null) {
                cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("public_id", fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'))));
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        String url = cloudinary.url().generate(image.getOriginalFilename());
        MediaResponse mediaResponse = new MediaResponse(url, "success");
        return new ResponseEntity<>(mediaResponse, HttpStatus.CREATED);
    }
}
