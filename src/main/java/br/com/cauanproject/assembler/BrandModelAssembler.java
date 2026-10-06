package br.com.cauanproject.assembler;

import br.com.cauanproject.controller.BrandController;
import br.com.cauanproject.entity.Brand;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BrandModelAssembler
        implements RepresentationModelAssembler<Brand, EntityModel<Brand>> {

    @Override
    public EntityModel<Brand> toModel(Brand brand) {
        EntityModel<Brand> model = EntityModel.of(brand);

        if (brand.getId() != null) {
            model.add(linkTo(methodOn(BrandController.class).buscarPorId(brand.getId())).withSelfRel());
        }

        model.add(linkTo(BrandController.class).withRel("marcas"));

        return model;
    }
}
