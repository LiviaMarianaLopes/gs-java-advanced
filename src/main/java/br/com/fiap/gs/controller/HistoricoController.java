package br.com.fiap.gs.controller;

import br.com.fiap.gs.dto.HistoricoRequest;
import br.com.fiap.gs.dto.HistoricoResponse;
import br.com.fiap.gs.model.Historico;
import br.com.fiap.gs.repository.HistoricoRepository;
import br.com.fiap.gs.service.HistoricoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/historico", produces = {"application/json"})
@Tag(name = "api-hostorico")
public class HistoricoController {
    @Autowired
    HistoricoMapper historicoMapper;
    @Autowired
    HistoricoRepository historicoRepository;
    @Operation(summary = "Cria um historico e grava no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Historico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Atributos informados são inválidos",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity<HistoricoResponse> cadastrar(@Validated @RequestBody HistoricoRequest request) {
        Historico historicoConvertido = historicoMapper.requestToHistorico(request);
        Historico historico = historicoRepository.save(historicoConvertido);
        if (historico.getId() != null) {
            HistoricoResponse historicoResponse = historicoMapper.historicoToResponse(historico);
            return new ResponseEntity<>(historicoResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Retorna todas os historicos cadastrados no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nenhum historico encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<EntityModel<HistoricoResponse>>> readHistoricos(
            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Historico> listaHistoricos = historicoRepository.findAll(pageable);
        if (listaHistoricos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<HistoricoResponse>> listaHistoricosResponse = new ArrayList<>();
        for (Historico historico : listaHistoricos) {
            HistoricoResponse historicoResponse = historicoMapper.historicoToResponse(historico);
            EntityModel<HistoricoResponse> historicoModel = EntityModel.of(historicoResponse,
                    linkTo(methodOn(HistoricoController.class)
                            .readHistorico(historico.getId())).withSelfRel(),
                    linkTo(methodOn(HistoricoController.class)
                            .delete(historico.getId())).withRel("Deletar historico"),
                    linkTo(methodOn(HistoricoController.class)
                            .update(historico.getId(), null)).withRel("Atualizar historico"),
                    linkTo(methodOn(HistoricoController.class)
                            .readHistoricos(0, 10)).withRel("Lista de historicos")
            );
            listaHistoricosResponse.add(historicoModel);
        }
        return new ResponseEntity<>(listaHistoricosResponse, HttpStatus.OK);
    }

    @Operation(summary = "Retorna um historico dado o seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nenhum historico encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<HistoricoResponse>> readHistorico(@PathVariable Long id) {
        Optional<Historico> historicoSalvo = historicoRepository.findById(id);
        if (historicoSalvo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        HistoricoResponse historicoResponse = historicoMapper.historicoToResponse(historicoSalvo.get());
        EntityModel<HistoricoResponse> historicoModel = EntityModel.of(historicoResponse,
                linkTo(methodOn(HistoricoController.class)
                        .readHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoController.class)
                        .delete(id)).withRel("Deletar historico"),
                linkTo(methodOn(HistoricoController.class)
                        .update(id, null)).withRel("Atualizar historico"),
                linkTo(methodOn(HistoricoController.class)
                        .readHistoricos(0, 10)).withRel("Lista de historicos")
        );
        return new ResponseEntity<>(historicoModel, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um historico já existente no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Historico não encontrado ou atributos informados são inválidos",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<HistoricoResponse> update(@PathVariable Long id, @Valid @RequestBody HistoricoRequest historicoRequest) {
        Optional<Historico> historicoSalvo = historicoRepository.findById(id);
        if (historicoSalvo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Historico historico = historicoMapper.requestToHistorico(historicoRequest);
        historico.setId(id);
        Historico historicoAtualizado = historicoRepository.save(historico);
        HistoricoResponse historicoResponse = historicoMapper.historicoToResponse(historicoAtualizado);
        return new ResponseEntity<>(historicoResponse, HttpStatus.OK);
    }

    @Operation(summary = "Exclui um historico do banco de dados dado um ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Historico não encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Exclusão realizada com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Historico> historicoSalvo = historicoRepository.findById(id);
        if (historicoSalvo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        historicoRepository.delete(historicoSalvo.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

