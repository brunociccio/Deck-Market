package br.com.deckmarket.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.deckmarket.controller.CardController;
import br.com.deckmarket.model.Card;

@Component
public class CardModelAssembler implements RepresentationModelAssembler<Card, EntityModel<Card>> {

    @Override
    public EntityModel<Card> toModel(Card card) {
        return EntityModel.of(card,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CardController.class).show(card.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CardController.class).index(null)).withRel("cards"));
    }
}
