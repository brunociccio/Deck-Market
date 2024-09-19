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
import br.com.deckmarket.assembler.PagamentoModelAssembler;
import br.com.deckmarket.model.Pagamento;
import br.com.deckmarket.repository.PagamentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamento", description = "APIs relacionadas ao gerenciamento de Pagamentos")
@Slf4j
public class PagamentoController {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private PagamentoModelAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Pagamento> pagedAssembler;

    @GetMapping
    @Operation(summary = "Listar todos os pagamentos", description = "Retorna uma lista de todos os pagamentos com paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pagamentos retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public PagedModel<EntityModel<Pagamento>> index(@ParameterObject Pageable pageable) {
        log.info("Listando todos os pagamentos com paginação");
        Page<Pagamento> page = repository.findAll(pageable);
        return pagedAssembler.toModel(page, assembler);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar pagamento por ID", description = "Retorna um pagamento específico pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagamento retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public EntityModel<Pagamento> show(@PathVariable Long id) {
        log.info("Buscando pagamento com id {}", id);
        var pagamento = repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Pagamento não encontrado")
        );
        return assembler.toModel(pagamento);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Criar um novo pagamento", description = "Cria um novo pagamento com os dados fornecidos")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Pagamento>> create(@RequestBody @Valid Pagamento pagamento) {
        log.info("Criando um novo pagamento");
        repository.save(pagamento);
        var entityModel = assembler.toModel(pagamento);
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar um pagamento", description = "Atualiza os dados de um pagamento existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagamento atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<EntityModel<Pagamento>> update(@PathVariable Long id, @RequestBody @Valid Pagamento pagamento) {
        log.info("Atualizando pagamento com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Pagamento não encontrado")
        );
        pagamento.setId(id);
        repository.save(pagamento);
        return ResponseEntity.ok(assembler.toModel(pagamento));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Excluir um pagamento", description = "Exclui um pagamento existente pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pagamento excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public void destroy(@PathVariable Long id) {
        log.info("Excluindo pagamento com id {}", id);
        repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Pagamento não encontrado")
        );
        repository.deleteById(id);
    }
}

