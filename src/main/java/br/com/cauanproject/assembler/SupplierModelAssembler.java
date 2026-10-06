package br.com.cauanproject.assembler;

import br.com.cauanproject.controller.SupplierController;
import br.com.cauanproject.entity.Supplier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SupplierModelAssembler
        implements RepresentationModelAssembler<Supplier, EntityModel<Supplier>> {

    @Override
    public EntityModel<Supplier> toModel(Supplier supplier) {
        EntityModel<Supplier> model = EntityModel.of(supplier);

        if (supplier.getId() != null) {
            model.add(linkTo(methodOn(SupplierController.class).buscarPorId(supplier.getId())).withSelfRel());
        }

        model.add(linkTo(SupplierController.class).withRel("fornecedores"));

        return model;
    }
}
