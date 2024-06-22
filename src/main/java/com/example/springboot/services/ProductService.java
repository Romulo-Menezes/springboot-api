package com.example.springboot.services;

import com.example.springboot.controllers.ProductController;
import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductModel createProduct(ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public List<ProductModel> getAllProducts() {
        List<ProductModel> productList = productRepository.findAll();
        if (!productList.isEmpty()) {
            for (ProductModel product: productList) {
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());

            }
        }
        return productList;
    }

    public Optional<ProductModel> getOneProduct(UUID id) {
        Optional<ProductModel> productO = productRepository.findById(id);
        productO.ifPresent(
                productModel -> productModel
                        .add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel()
                )
        );
        return productO;
    }

    public Optional<ProductModel> updateProduct(UUID id, ProductRecordDto productRecordDto) {
        Optional<ProductModel> productO = getOneProduct(id);
        if (productO.isPresent()) {
            var productModel = productO.get();
            BeanUtils.copyProperties(productRecordDto, productModel);
            return Optional.of(productRepository.save(productModel));
        }
        return productO;
    }

    public boolean deleteProduct(UUID id) {
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isPresent()) {
            productRepository.delete(productO.get());
            return true;
        }
        return false;
    }

}
