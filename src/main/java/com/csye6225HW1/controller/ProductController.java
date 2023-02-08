package com.csye6225HW1.controller;

import com.csye6225HW1.POJO.Product;

import com.csye6225HW1.service.impl.ProductServiceImplMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    @Autowired
    ProductServiceImplMP productService;

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @PostMapping("/")
    public ResponseEntity<Product> InitialProduct(@RequestBody Product product) {
        Product newProduct = productService.createProduct(product);
        return ResponseEntity.status(200).body(newProduct);
    }

    @PutMapping("/{productId}")
    public String updateProduct(@PathVariable Long productId,@RequestBody Product product){
        productService.updateProduct(productId,product);
        return "Updated successfully";
    }

    @PatchMapping("/{productId}")
    public String patchProduct(@PathVariable Long productId,@RequestBody Product product){
        productService.updateProduct(productId,product);
        return "Updated successfully";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return "Deleted successfully ";
    }



}
