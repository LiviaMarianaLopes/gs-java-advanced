package br.com.fiap.gs.dto;

import br.com.fiap.gs.model.Login;

public record CadastroResponse(
        Long id,
        String nome,
        String email,
        Long rg,
        Long cpf,
        String senha,
        Login login
) {
}
