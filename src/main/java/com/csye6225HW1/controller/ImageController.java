package com.csye6225HW1.controller;

import com.csye6225HW1.POJO.Image;
import com.csye6225HW1.POJO.Product;
import com.csye6225HW1.service.impl.ImageServiceImplMP;
import com.csye6225HW1.service.impl.ProductServiceImplMP;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/v1")
public class ImageController {


    @Autowired
    private StatsDClient metricsClient;

    @Autowired
    private ImageServiceImplMP imageService;



    @GetMapping("/product/{productId}/image/{imageId}")
    public Image getImage(HttpServletRequest request, @PathVariable Long productId, @PathVariable String imageId) throws IOException {
        log.info("Info message: get image");
        metricsClient.incrementCounter("endpoint.imageId.http.get");
        Image image = imageService.getImage(productId, imageId);
        Long elapsedTime = (Long) request.getAttribute("elapsedTime");
        metricsClient.recordExecutionTime("endpoint.imageId.http.get.timer", elapsedTime);
        log.info("Info message: get image; ElapsedTime = " + elapsedTime);
        return image;
    }


    @GetMapping("/product/{productId}/image")
    public List<Image> getImagesList(@PathVariable Long productId) {
        log.info("Info message:get image list");
        metricsClient.incrementCounter("endpoint.imageList.http.get");
        long startTime = System.currentTimeMillis();
        List<Image> image= imageService.getImageList(productId);
        long elapsedTime = System.currentTimeMillis() - startTime;
        metricsClient.recordExecutionTime("endpoint.imageId.http.get.timer", elapsedTime);
        log.info("Info message:get image List；ElapsedTime = " +elapsedTime);

        return image;
    }



    @PostMapping(value = "/product/{productId}/image", consumes = "multipart/form-data")
    public ResponseEntity<Image> uploadImage(@PathVariable("productId") Long productId,
                                             @RequestParam("file") MultipartFile file) throws IOException {
        log.info("Info message:post image");
        metricsClient.incrementCounter("endpoint.image.http.post");
        long startTime = System.currentTimeMillis();
        Image imageResponse = imageService.uploadImage(productId, file);
        long elapsedTime = System.currentTimeMillis() - startTime;
        metricsClient.recordExecutionTime("endpoint.imageId.http.get.timer", elapsedTime);

        log.info("Info message:post image；ElapsedTime = " +elapsedTime);
        return ResponseEntity.status(200).body(imageResponse);
    }








    @DeleteMapping(value = "/product/{productId}/image/{imageId}")
    public String deleteImage(@PathVariable("productId") Long productId,
                                            @PathVariable("imageId") String imageId) throws IOException {
        log.info("Info message:delete image");
        metricsClient.incrementCounter("endpoint.image.http.delete");

        long startTime = System.currentTimeMillis();

        imageService.deleteImage(productId, imageId);
        long elapsedTime = System.currentTimeMillis() - startTime;
        metricsClient.recordExecutionTime("endpoint.imageId.http.get.timer", elapsedTime);

        log.info("Info message:delete image；ElapsedTime = " +elapsedTime);
        return "Your image was successfully deleted ";
    }
}
