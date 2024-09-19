package br.com.deckmarket.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.deckmarket.controller.CarrinhoController;
import br.com.deckmarket.model.Carrinho;

@Component
public class CarrinhoModelAssembler implements RepresentationModelAssembler<Carrinho, EntityModel<Carrinho>> {

    @Override
    public EntityModel<Carrinho> toModel(Carrinho carrinho) {
        return EntityModel.of(carrinho,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CarrinhoController.class).show(carrinho.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CarrinhoController.class).index(null)).withRel("carrinhos"));
    }
}
