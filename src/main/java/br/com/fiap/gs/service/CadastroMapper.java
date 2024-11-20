package br.com.fiap.gs.service;

import br.com.fiap.gs.dto.CadastroRequest;
import br.com.fiap.gs.dto.CadastroResponse;
import br.com.fiap.gs.model.Cadastro;
import br.com.fiap.gs.model.Login;
import br.com.fiap.gs.repository.LoginRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CadastroMapper {
    @Autowired
    private LoginRepository loginRepository;
    @Transactional
    public Cadastro requestToCadastro(CadastroRequest request) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(request.senha());
        Login novoLogin = new Login(request.email(), encryptedPassword);
        Login loginSalvo = loginRepository.save(novoLogin);

        Cadastro novoCadastro = new Cadastro();
        novoCadastro.setNome(request.nome());
        novoCadastro.setEmail(request.email());
        novoCadastro.setRg(Long.parseLong(request.rg()));
        novoCadastro.setCpf(Long.parseLong(request.cpf()));
        novoCadastro.setSenha(request.senha());
        novoCadastro.setLogin(loginSalvo);

        return novoCadastro;
    }

    public CadastroResponse cadastroToResponse(Cadastro cadastro) {
        return new CadastroResponse(
                cadastro.getId(),
                cadastro.getNome(),
                cadastro.getEmail(),
                cadastro.getRg(),
                cadastro.getCpf(),
                cadastro.getSenha(),
                cadastro.getLogin()
        );
    }
}
