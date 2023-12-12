package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Image;
import com.shop.ua.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.ClassPathResource;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class WebImageController {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private RepositoryManager repositoryManager;
    @Autowired
    private final ResourceLoader resourceLoader;

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws IOException {
        Image image = repositoryManager.getGoodsService().getImageById(id);

        if (image != null) {
            File file = new File(image.getImagePath());
            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .header("Content-Type", image.getContentType())
                    .body(resource);
        } else {
            // Обробка випадку, коли зображення не знайдено
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        ClassPathResource imgFile = new ClassPathResource("static/images/" + imageName);

        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG) // або MediaType.IMAGE_PNG, залежно від формату вашого зображення
                .body(bytes);
    }


}
