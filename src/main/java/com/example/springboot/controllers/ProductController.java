package com.example.springboot.controllers;

import com.example.springboot.assemblers.ProductModelAssembler;
import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ProductController {
    private final ProductService productService;
    private final PagedResourcesAssembler<ProductModel> pagedResourcesAssembler;
    private final ProductModelAssembler productModelAssembler;
    private final int pageSize;
    private final String sortBy;

    public ProductController(ProductService productService,
                             PagedResourcesAssembler<ProductModel> pagedResourcesAssembler,
                             ProductModelAssembler productModelAssembler) {
        this.productService = productService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.productModelAssembler = productModelAssembler;
        this.pageSize = 20;
        this.sortBy = "createdAt";
    }

    @PostMapping("/products")
    public ResponseEntity<ProductModel> createProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(productRecordDto));
    }

    @GetMapping("/products")
    public ResponseEntity<PagedModel<EntityModel<ProductModel>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, this.pageSize, Sort.by(Sort.Direction.DESC, this.sortBy));
        Page<ProductModel> products = productService.getAllProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pagedResourcesAssembler.toModel(products, productModelAssembler));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        return productService.getOneProduct(id, this.pageSize)
                .<ResponseEntity<Object>>map(productModel -> ResponseEntity.status(HttpStatus.OK).body(productModel))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        return productService.updateProduct(id, productRecordDto, this.pageSize)
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

