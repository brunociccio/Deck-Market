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

import br.com.deckmarket.assembler.PedidoModelAssembler;
import br.com.deckmarket.model.Pedido;
import br.com.deckmarket.repository.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedido", description = "APIs relacionadas ao gerenciamento de Pedidos")
@Slf4j
public class PedidoController {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private PedidoModelAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Pedido> pagedAssembler;

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista de todos os pedidos com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public PagedModel<EntityModel<Pedido>> index(@ParameterObject Pageable pageable) {
        log.info("Listando todos os pedidos com paginação");
        Page<Pedido> page = repository.findAll(pageable);
        return pagedAssembler.toModel(page, assembler);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public EntityModel<Pedido> show(@PathVariable Long id) {
        log.info("Buscando pedido com id {}", id);
        var pedido = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Pedido não encontrado")
        );
        return assembler.toModel(pedido);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido com os dados fornecidos")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Pedido>> create(@RequestBody @Valid Pedido pedido) {
        log.info("Criando um novo pedido");
        repository.save(pedido);
        var entityModel = assembler.toModel(pedido);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar um pedido", description = "Atualiza os dados de um pedido existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Pedido>> update(@PathVariable Long id, @RequestBody @Valid Pedido pedido) {
        log.info("Atualizando pedido com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Pedido não encontrado")
        );
        pedido.setId(id);
        repository.save(pedido);
        return ResponseEntity.ok(assembler.toModel(pedido));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Excluir um pedido", description = "Exclui um pedido existente pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void destroy(@PathVariable Long id) {
        log.info("Excluindo pedido com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Pedido não encontrado")
        );
        repository.deleteById(id);
    }
}
