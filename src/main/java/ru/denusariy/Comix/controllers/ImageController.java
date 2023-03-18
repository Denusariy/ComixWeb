package ru.denusariy.Comix.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.denusariy.Comix.domain.entity.Image;
import ru.denusariy.Comix.exception.ImageNotFoundException;
import ru.denusariy.Comix.repositories.ImageRepository;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;

    @GetMapping("/images/{id}")
    @Operation(summary = "Открытие изображения с указанным id")
    private ResponseEntity<?> getImageById(@PathVariable int id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(
                String.format("Изображение с id %d не найдено!", id)));
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
