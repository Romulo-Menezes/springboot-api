package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductModel> createProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(productRecordDto));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        return productService.getOneProduct(id)
                .<ResponseEntity<Object>>map(productModel -> ResponseEntity.status(HttpStatus.OK).body(productModel))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        return productService.updateProduct(id, productRecordDto)
                .<ResponseEntity<Object>>map(productModel -> ResponseEntity.status(HttpStatus.OK).body(productModel))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
    }
}

