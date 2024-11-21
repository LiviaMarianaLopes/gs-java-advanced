package br.com.fiap.gs.controller;

import br.com.fiap.gs.dto.AuthDTO;
import br.com.fiap.gs.dto.CadastroRequest;
import br.com.fiap.gs.dto.CadastroResponse;
import br.com.fiap.gs.dto.LoginResponse;
import br.com.fiap.gs.model.Cadastro;
import br.com.fiap.gs.model.Login;
import br.com.fiap.gs.repository.CadastroRepository;
import br.com.fiap.gs.repository.LoginRepository;
import br.com.fiap.gs.security.TokenService;
import br.com.fiap.gs.service.CadastroMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/cadastro", produces = {"application/json"})
@Tag(name = "api-cadastro")
public class CadastroController {
    @Autowired
    CadastroMapper cadastroMapper;
    @Autowired
    CadastroRepository cadastroRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private TokenService tokenService;
    @Operation(
            summary = "Autentica o usuário e retorna um token JWT",
            description = "Realiza a autenticação de um usuário com base no email e senha fornecidos. Se as credenciais forem válidas, retorna um token JWT e o ID do cadastro associado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticação realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDTO authDTO) {
        try {
            var usuarioSenha = new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.senha());
            var auth = this.authenticationManager.authenticate(usuarioSenha);
            var token = tokenService.generateToken((Login) auth.getPrincipal());
            Cadastro cadastro = cadastroRepository.findByEmail(authDTO.email())
                    .orElseThrow(() -> new RuntimeException("Cadastro não encontrado"));
            ;
            return ResponseEntity.ok(new LoginResponse(token, cadastro.getId()));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Credenciais inválidas");
        }
    }

    @Operation(summary = "Cria um cadastro e grava no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Atributos informados são inválidos",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity cadastrar(@Validated @RequestBody CadastroRequest request) {
        if (loginRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("O email já foi cadastrado");
        }
        Cadastro cadastroConvertido = cadastroMapper.requestToCadastro(request);
        Cadastro cadastro = cadastroRepository.save(cadastroConvertido);
        if (cadastro.getId() != null) {
            CadastroResponse cadastroResponse = cadastroMapper.cadastroToResponse(cadastro);
            return new ResponseEntity<>(cadastroResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Retorna todas os cadastros cadastrados no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nenhuma cadastro encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<EntityModel<CadastroResponse>>> readCadastros(
            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Cadastro> listaCadastros = cadastroRepository.findAll(pageable);

        if (listaCadastros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<CadastroResponse>> listaCadastrosResponse = new ArrayList<>();
        for (Cadastro cadastro : listaCadastros) {
            CadastroResponse cadastroResponse = cadastroMapper.cadastroToResponse(cadastro);
            EntityModel<CadastroResponse> cadastroModel = EntityModel.of(cadastroResponse,
                    linkTo(methodOn(CadastroController.class)
                            .readCadastro(cadastro.getId())).withSelfRel(),
                    linkTo(methodOn(CadastroController.class)
                            .delete(cadastro.getId())).withRel("Deletar cadastro"),
                    linkTo(methodOn(CadastroController.class)
                            .update(cadastro.getId(), null)).withRel("Atualizar cadastro"),
                    linkTo(methodOn(CadastroController.class)
                            .readCadastros(0, 10)).withRel("Lista de cadastros")
            );
            listaCadastrosResponse.add(cadastroModel);
        }
        return new ResponseEntity<>(listaCadastrosResponse, HttpStatus.OK);
    }

    @Operation(summary = "Retorna um cadastro dado o seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nenhum cadastro encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CadastroResponse>> readCadastro(@PathVariable Long id) {
        Optional<Cadastro> cadastroSalvo = cadastroRepository.findById(id);
        if (cadastroSalvo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        CadastroResponse cadastroResponse = cadastroMapper.cadastroToResponse(cadastroSalvo.get());
        EntityModel<CadastroResponse> cadastroModel = EntityModel.of(cadastroResponse,
                linkTo(methodOn(CadastroController.class)
                        .readCadastro(id)).withSelfRel(),
                linkTo(methodOn(CadastroController.class)
                        .delete(id)).withRel("Deletar cadastro"),
                linkTo(methodOn(CadastroController.class)
                        .update(id, null)).withRel("Atualizar cadastro"),
                linkTo(methodOn(CadastroController.class)
                        .readCadastros(0, 10)).withRel("Lista de cadastros")
        );
        return new ResponseEntity<>(cadastroModel, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um cadastro já existente no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Cadastro não encontrado ou atributos informados são inválidos",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CadastroResponse> update(@PathVariable Long id, @Valid @RequestBody CadastroRequest cadastroRequest) {
        Cadastro cadastroExistente = cadastroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cadastro não encontrado para atualização"));

            cadastroExistente.getLogin().setEmail(cadastroRequest.email());
            String encryptedPassword = new BCryptPasswordEncoder().encode(cadastroRequest.senha());
            cadastroExistente.getLogin().setSenha(encryptedPassword);
            cadastroExistente.setNome(cadastroRequest.nome());
            cadastroExistente.setEmail(cadastroRequest.email());
            cadastroExistente.setSenha(cadastroRequest.senha());
            cadastroExistente.setRg(Long.parseLong(cadastroRequest.rg()));
            cadastroExistente.setCpf(Long.parseLong(cadastroRequest.cpf()));

        cadastroRepository.save(cadastroExistente);
        CadastroResponse cadastroResponse = cadastroMapper.cadastroToResponse(cadastroExistente);
        return new ResponseEntity<>(cadastroResponse, HttpStatus.OK);
    }

    @Operation(summary = "Exclui um cadastro do banco de dados dado um ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Cadastro não encontrado",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "200", description = "Exclusão realizada com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Cadastro> cadastroSalvo = cadastroRepository.findById(id);
        if (cadastroSalvo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        cadastroRepository.delete(cadastroSalvo.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
