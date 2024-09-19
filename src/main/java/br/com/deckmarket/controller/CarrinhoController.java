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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.deckmarket.assembler.CarrinhoModelAssembler;
import br.com.deckmarket.model.Carrinho;
import br.com.deckmarket.repository.CarrinhoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/carrinhos")
@Tag(name = "Carrinho", description = "APIs relacionadas ao gerenciamento de Carrinhos")
@Slf4j
public class CarrinhoController {

    @Autowired
    private CarrinhoRepository repository;

    @Autowired
    private CarrinhoModelAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Carrinho> pagedAssembler;

    @GetMapping
    @Operation(summary = "Listar todos os carrinhos", description = "Retorna uma lista de todos os carrinhos com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de carrinhos retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public PagedModel<EntityModel<Carrinho>> index(@ParameterObject Pageable pageable) {
        log.info("Listando todos os carrinhos com paginação");
        Page<Carrinho> page = repository.findAll(pageable);
        return pagedAssembler.toModel(page, assembler);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar carrinho por ID", description = "Retorna um carrinho específico pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carrinho retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Carrinho não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public EntityModel<Carrinho> show(@PathVariable Long id) {
        log.info("Buscando carrinho com id {}", id);
        var carrinho = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Carrinho não encontrado")
        );
        return assembler.toModel(carrinho);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Criar um novo carrinho", description = "Cria um novo carrinho com os dados fornecidos")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Carrinho criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Carrinho>> create(@RequestBody @Valid Carrinho carrinho) {
        log.info("Criando um novo carrinho");
        repository.save(carrinho);
        var entityModel = assembler.toModel(carrinho);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar um carrinho", description = "Atualiza os dados de um carrinho existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carrinho atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Carrinho não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Carrinho>> update(@PathVariable Long id, @RequestBody @Valid Carrinho carrinho) {
        log.info("Atualizando carrinho com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Carrinho não encontrado")
        );
        carrinho.setId(id);
        repository.save(carrinho);
        return ResponseEntity.ok(assembler.toModel(carrinho));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Excluir um carrinho", description = "Exclui um carrinho existente pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Carrinho excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Carrinho não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void destroy(@PathVariable Long id) {
        log.info("Excluindo carrinho com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Carrinho não encontrado")
        );
        repository.deleteById(id);
    }
}


