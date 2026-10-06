package br.com.cauanproject.assembler;

import br.com.cauanproject.controller.ProductController;
import br.com.cauanproject.entity.Product;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductModelAssembler
        implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product product) {

        return EntityModel.of(
                product,

                linkTo(
                        methodOn(ProductController.class)
                                .buscarPorId(product.getId())
                ).withSelfRel()
        );
    }
}