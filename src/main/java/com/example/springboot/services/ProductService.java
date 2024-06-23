package com.example.springboot.services;

import com.example.springboot.controllers.ProductController;
import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<ProductModel> getAllProducts(Pageable pageable) {
        Page<ProductModel> productPage = productRepository.findAll(pageable);
        if (!productPage.isEmpty()) {
            for (ProductModel product: productPage) {
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());

            }
        }
        return productPage;
    }

    public Optional<ProductModel> getOneProduct(UUID id, int pageSize) {
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isPresent()) {
            int page = (productRepository.getIndexById(productO.get().getIdProduct()) - 1) / pageSize;
            productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts(page)).withSelfRel());
        }
        return productO;
    }

    public Optional<ProductModel> updateProduct(UUID id, ProductRecordDto productRecordDto, int pageSize) {
        Optional<ProductModel> productO = getOneProduct(id, pageSize);
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
