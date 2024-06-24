package com.example.springboot.assemblers;

import com.example.springboot.controllers.ProductController;
import com.example.springboot.models.ProductModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<ProductModel, EntityModel<ProductModel>> {

    @Override
    public EntityModel<ProductModel> toModel(ProductModel productModel) {
        return EntityModel.of(productModel,
                linkTo(methodOn(ProductController.class).getOneProduct(productModel.getIdProduct()))
                        .withSelfRel());
    }
}
