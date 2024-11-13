package br.com.fiap.gs.controller;

import br.com.fiap.gs.dto.AuthDTO;
import br.com.fiap.gs.dto.LoginResponse;
import br.com.fiap.gs.dto.RegisterDTO;
import br.com.fiap.gs.model.Login;
import br.com.fiap.gs.repository.LoginRepository;
import br.com.fiap.gs.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDTO authDTO) {
        try {
            var usuarioSenha = new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.senha());
            var auth = this.authenticationManager.authenticate(usuarioSenha);
            var token = tokenService.generateToken((Login) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Credenciais inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        if (loginRepository.findByEmail(registerDTO.email()) != null) {
            return ResponseEntity
                    .badRequest()
                    .body("O email já foi cadastrado");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.senha());
        Login novoUsuario = new Login(registerDTO.email(), encryptedPassword);
        loginRepository.save(novoUsuario);
        return ResponseEntity.ok().build();
    }
}
