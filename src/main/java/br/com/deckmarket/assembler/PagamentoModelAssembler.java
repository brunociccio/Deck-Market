package br.com.deckmarket.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.deckmarket.controller.PagamentoController;
import br.com.deckmarket.model.Pagamento;

@Component
public class PagamentoModelAssembler implements RepresentationModelAssembler<Pagamento, EntityModel<Pagamento>> {

    @Override
    public EntityModel<Pagamento> toModel(Pagamento pagamento) {
        return EntityModel.of(pagamento,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagamentoController.class).show(pagamento.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PagamentoController.class).index(null)).withRel("pagamentos"));
    }
}
