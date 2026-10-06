package br.com.cauanproject.assembler;

import br.com.cauanproject.controller.CategoryController;
import br.com.cauanproject.entity.Category;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryModelAssembler
        implements RepresentationModelAssembler<Category, EntityModel<Category>> {

    @Override
    public EntityModel<Category> toModel(Category category) {
        EntityModel<Category> model = EntityModel.of(category);

        if (category.getId() != null) {
            model.add(linkTo(methodOn(CategoryController.class).buscarPorId(category.getId())).withSelfRel());
        }

        model.add(linkTo(CategoryController.class).withRel("categorias"));

        return model;
    }
}
