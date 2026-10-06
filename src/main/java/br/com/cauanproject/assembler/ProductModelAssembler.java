package br.com.cauanproject.assembler;

import br.com.cauanproject.controller.BrandController;
import br.com.cauanproject.controller.CategoryController;
import br.com.cauanproject.controller.ProductController;
import br.com.cauanproject.controller.StockController;
import br.com.cauanproject.entity.Product;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler
        implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product product) {
        EntityModel<Product> model = EntityModel.of(product);

        if (product.getId() != null) {
            model.add(linkTo(methodOn(ProductController.class).buscarPorId(product.getId())).withSelfRel());
        }

        model.add(linkTo(ProductController.class).withRel("produtos"));

        if (product.getBrand() != null && product.getBrand().getId() != null) {
            model.add(linkTo(methodOn(BrandController.class).buscarPorId(product.getBrand().getId())).withRel("brand"));
        }

        if (product.getCategory() != null && product.getCategory().getId() != null) {
            model.add(linkTo(methodOn(CategoryController.class).buscarPorId(product.getCategory().getId())).withRel("category"));
        }

        if (product.getStock() != null && product.getStock().getId() != null) {
            model.add(linkTo(methodOn(StockController.class).buscarPorId(product.getStock().getId())).withRel("stock"));
        }

        return model;
    }
}