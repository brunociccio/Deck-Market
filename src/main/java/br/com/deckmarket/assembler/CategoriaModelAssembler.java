package br.com.deckmarket.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.deckmarket.controller.CategoriaController;
import br.com.deckmarket.model.Categoria;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {

    @Override
    public EntityModel<Categoria> toModel(Categoria categoria) {
        return EntityModel.of(categoria,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoriaController.class).show(categoria.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CategoriaController.class).index(null)).withRel("categorias"));
    }
}
