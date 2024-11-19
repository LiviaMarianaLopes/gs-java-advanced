package br.com.fiap.gs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CadastroRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @Email(message = "O email deve ser válido")
        @NotBlank(message = "O email não pode estar vazio")
        String email,
        Long rg,
        Long cpf,
        @NotBlank(message = "A senha não pode estar vazia")
        String senha
) {
}
