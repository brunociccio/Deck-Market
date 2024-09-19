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
import br.com.deckmarket.assembler.CategoriaModelAssembler;
import br.com.deckmarket.model.Categoria;
import br.com.deckmarket.repository.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categoria", description = "APIs relacionadas ao gerenciamento de Categorias de Cards")
@Slf4j
public class CategoriaController {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private CategoriaModelAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Categoria> pagedAssembler;

    @GetMapping
    @Operation(summary = "Listar todas as categorias", description = "Retorna uma lista de todas as categorias com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public PagedModel<EntityModel<Categoria>> index(@ParameterObject Pageable pageable) {
        log.info("Listando todas as categorias com paginação");
        Page<Categoria> page = repository.findAll(pageable);
        return pagedAssembler.toModel(page, assembler);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria específica pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public EntityModel<Categoria> show(@PathVariable Long id) {
        log.info("Buscando categoria com id {}", id);
        var categoria = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Categoria não encontrada")
        );
        return assembler.toModel(categoria);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Criar uma nova categoria", description = "Cria uma nova categoria com os dados fornecidos")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Categoria>> create(@RequestBody @Valid Categoria categoria) {
        log.info("Criando uma nova categoria");
        repository.save(categoria);
        var entityModel = assembler.toModel(categoria);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar uma categoria", description = "Atualiza os dados de uma categoria existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Categoria>> update(@PathVariable Long id, @RequestBody @Valid Categoria categoria) {
        log.info("Atualizando categoria com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Categoria não encontrada")
        );
        categoria.setId(id);
        repository.save(categoria);
        return ResponseEntity.ok(assembler.toModel(categoria));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Excluir uma categoria", description = "Exclui uma categoria existente pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void destroy(@PathVariable Long id) {
        log.info("Excluindo categoria com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Categoria não encontrada")
        );
        repository.deleteById(id);
    }
}

