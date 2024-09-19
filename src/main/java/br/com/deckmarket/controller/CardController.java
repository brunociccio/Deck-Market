package br.com.deckmarket.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.deckmarket.assembler.CardModelAssembler;
import br.com.deckmarket.model.Card;
import br.com.deckmarket.repository.CardRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cards")
@Tag(name = "Card", description = "APIs relacionadas ao gerenciamento de Cards")
@Slf4j
public class CardController {

    @Autowired
    private CardRepository repository;

    @Autowired
    private CardModelAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Card> pagedAssembler;

    @GetMapping
    @Operation(summary = "Listar todos os cards", description = "Retorna uma lista de todos os cards com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de cards retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public PagedModel<EntityModel<Card>> index(@ParameterObject Pageable pageable) {
        log.info("Listando todos os cards com paginação");
        Page<Card> page = repository.findAll(pageable);
        return pagedAssembler.toModel(page, assembler);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar card por ID", description = "Retorna um card específico pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Card retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Card não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public EntityModel<Card> show(@PathVariable Long id) {
        log.info("Buscando card com id {}", id);
        var card = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Card não encontrado")
        );
        return assembler.toModel(card);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Criar um novo card", description = "Cria um novo card com os dados fornecidos")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Card criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Card>> create(@RequestBody @Valid Card card) {
        log.info("Criando um novo card");
        repository.save(card);
        var entityModel = assembler.toModel(card);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar um card", description = "Atualiza os dados de um card existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Card atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Card não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Card>> update(@PathVariable Long id, @RequestBody @Valid Card card) {
        log.info("Atualizando card com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Card não encontrado")
        );
        card.setId(id);
        repository.save(card);
        return ResponseEntity.ok(assembler.toModel(card));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Excluir um card", description = "Exclui um card existente pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Card excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Card não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void destroy(@PathVariable Long id) {
        log.info("Excluindo card com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Card não encontrado")
        );
        repository.deleteById(id);
    }
}

