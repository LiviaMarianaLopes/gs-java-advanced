package br.com.fiap.gs.controller;

import br.com.fiap.gs.dto.VeiculoRequest;
import br.com.fiap.gs.dto.VeiculoResponse;
import br.com.fiap.gs.model.Veiculo;
import br.com.fiap.gs.repository.VeiculoRepository;
import br.com.fiap.gs.service.VeiculoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/veiculo", produces = {"application/json"})
@Tag(name = "api-veiculo")
public class VeiculoController {
    @Autowired
    VeiculoMapper veiculoMapper;
    @Autowired
    VeiculoRepository veiculoRepository;
    @Operation(summary = "Cria um veiculo e grava no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veiculo cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Atributos informados são inválidos",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity<VeiculoResponse> cadastrar(@Validated @RequestBody VeiculoRequest request) {
        Veiculo veiculoConvertido = veiculoMapper.requestToVeiculo(request);
        Veiculo veiculo = veiculoRepository.save(veiculoConvertido);
        if (veiculo.getId() != null) {
            VeiculoResponse veiculoResponse = veiculoMapper.veiculoToResponse(veiculo);
            return new ResponseEntity<>(veiculoResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Retorna todas os veiculos cadastrados no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nenhum veiculo encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<EntityModel<VeiculoResponse>>> readVeiculos() {
        List<Veiculo> listaVeiculos = veiculoRepository.findAll();
        if (listaVeiculos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<VeiculoResponse>> listaVeiculosResponse = new ArrayList<>();
        for (Veiculo veiculo : listaVeiculos) {
            VeiculoResponse veiculoResponse = veiculoMapper.veiculoToResponse(veiculo);
            EntityModel<VeiculoResponse> veiculoModel = EntityModel.of(veiculoResponse,
                    linkTo(methodOn(VeiculoController.class)
                            .readVeiculo(veiculo.getId())).withSelfRel(),
                    linkTo(methodOn(VeiculoController.class)
                            .delete(veiculo.getId())).withRel("Deletar veiculo"),
                    linkTo(methodOn(VeiculoController.class)
                            .update(veiculo.getId(), null)).withRel("Atualizar veiculo"),
                    linkTo(methodOn(VeiculoController.class)
                            .readVeiculos()).withRel("Lista de veiculos")
            );
            listaVeiculosResponse.add(veiculoModel);
        }
        return new ResponseEntity<>(listaVeiculosResponse, HttpStatus.OK);
    }

    @Operation(summary = "Retorna um veiculo dado o seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nenhum veiculo encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<VeiculoResponse>> readVeiculo(@PathVariable Long id) {
        Optional<Veiculo> veiculoSalvo = veiculoRepository.findById(id);
        if (veiculoSalvo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        VeiculoResponse veiculoResponse = veiculoMapper.veiculoToResponse(veiculoSalvo.get());
        EntityModel<VeiculoResponse> veiculoModel = EntityModel.of(veiculoResponse,
                linkTo(methodOn(VeiculoController.class)
                        .readVeiculo(id)).withSelfRel(),
                linkTo(methodOn(VeiculoController.class)
                        .delete(id)).withRel("Deletar veiculo"),
                linkTo(methodOn(VeiculoController.class)
                        .update(id, null)).withRel("Atualizar veiculo"),
                linkTo(methodOn(VeiculoController.class)
                        .readVeiculos()).withRel("Lista de veiculos")
        );
        return new ResponseEntity<>(veiculoModel, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um veiculo já existente no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Veiculo não encontrado ou atributos informados são inválidos",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> update(@PathVariable Long id, @Valid @RequestBody VeiculoRequest veiculoRequest) {
        Optional<Veiculo> veiculoSalvo = veiculoRepository.findById(id);
        if (veiculoSalvo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Veiculo veiculo = veiculoMapper.requestToVeiculo(veiculoRequest);
        veiculo.setId(id);
        Veiculo veiculoAtualizado = veiculoRepository.save(veiculo);
        VeiculoResponse veiculoResponse = veiculoMapper.veiculoToResponse(veiculoAtualizado);
        return new ResponseEntity<>(veiculoResponse, HttpStatus.OK);
    }

    @Operation(summary = "Exclui um veiculo do banco de dados dado um ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Veiculo não encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Exclusão realizada com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Veiculo> veiculoSalvo = veiculoRepository.findById(id);
        if (veiculoSalvo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        veiculoRepository.delete(veiculoSalvo.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

