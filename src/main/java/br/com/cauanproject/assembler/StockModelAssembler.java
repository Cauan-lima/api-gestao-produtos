package br.com.cauanproject.assembler;

import br.com.cauanproject.controller.StockController;
import br.com.cauanproject.entity.Stock;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StockModelAssembler
        implements RepresentationModelAssembler<Stock, EntityModel<Stock>> {

    @Override
    public EntityModel<Stock> toModel(Stock stock) {
        EntityModel<Stock> model = EntityModel.of(stock);

        if (stock.getId() != null) {
            model.add(linkTo(methodOn(StockController.class).buscarPorId(stock.getId())).withSelfRel());
        }

        model.add(linkTo(StockController.class).withRel("estoques"));

        return model;
    }
}
