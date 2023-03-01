package com.csye6225HW1.controller;

import com.csye6225HW1.POJO.Image;
import com.csye6225HW1.POJO.Product;
import com.csye6225HW1.service.impl.ImageServiceImplMP;
import com.csye6225HW1.service.impl.ProductServiceImplMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class ImageController {

    @Autowired
    private ImageServiceImplMP imageService;

    @GetMapping("/product/{productId}/image/{imageId}")
    public Image getImage(@PathVariable Long productId, @PathVariable String imageId) throws IOException {
        return imageService.getImage(productId, imageId);
    }

    @GetMapping("/product/{productId}/image")
    public List<Image> getImagesList(@PathVariable Long productId) {
        return imageService.getImageList(productId);
    }



    @PostMapping(value = "/product/{productId}/image", consumes = "multipart/form-data")
    public ResponseEntity<Image> uploadImage(@PathVariable("productId") Long productId,
                                             @RequestParam("file") MultipartFile file) throws IOException {
        Image imageResponse = imageService.uploadImage(productId, file);
        return ResponseEntity.status(200).body(imageResponse);
    }








    @DeleteMapping(value = "/product/{productId}/image/{imageId}")
    public String deleteImage(@PathVariable("productId") Long productId,
                                            @PathVariable("imageId") String imageId) throws IOException {
        imageService.deleteImage(productId, imageId);
        return "Your image was successfully deleted ";
    }
}
