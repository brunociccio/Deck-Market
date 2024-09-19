package br.com.deckmarket.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.deckmarket.controller.PedidoController;
import br.com.deckmarket.model.Pedido;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(pedido,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).show(pedido.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PedidoController.class).index(null)).withRel("pedidos"));
    }
}
