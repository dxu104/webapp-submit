package com.csye6225HW1.controller;

import com.csye6225HW1.POJO.Product;

import com.csye6225HW1.service.impl.ProductServiceImplMP;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/v1/product")
public class ProductController {


    @Autowired
    ProductServiceImplMP productService;
    @Autowired
    private StatsDClient metricsClient;


    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        log.info("Info message:get product");
        metricsClient.incrementCounter("endpoint.product.http.get");
        return productService.getProduct(productId);
    }

    @PostMapping("/")
    //@ResponseBody可以直接返回Json结果，
    //@ResponseEntity不仅可以返回json结果，还可以定义返回的HttpHeaders和HttpStatus
    public ResponseEntity<Product> InitialProduct(@RequestBody Product product) {
        log.info("Info message:post product");
        metricsClient.incrementCounter("endpoint.product.http.post");
        Product newProduct = productService.createProduct(product);
        return ResponseEntity.status(200).body(newProduct);
    }

    @PutMapping("/{productId}")
    public String updateProduct(@PathVariable Long productId,@RequestBody Product product){
        log.info("Info message:put product");
        metricsClient.incrementCounter("endpoint.product.http.put");
        productService.updateProduct(product,productId);
        return "Your product is already updated";
       }

    @PatchMapping("/{productId}")
    //在Spring Framework中，可以使用@RequestBody注解来将请求正文中的JSON数据映射为Java对象。例如
    public String patchProduct(@PathVariable Long productId,@RequestBody Product product){
        log.info("Info message:patch product");
        metricsClient.incrementCounter("endpoint.product.http.patch");
        productService.patchProduct(product,productId);
        return "Your product is already patched ";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Long productId){
        log.info("Info message:delete product");
        metricsClient.incrementCounter("endpoint.product.http.delete");
        productService.deleteProduct(productId);
        return "Your product is already deleted ";
    }



}
