package br.com.fiap.gs.dto;

import jakarta.validation.constraints.*;

public record CadastroRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @Email(message = "O email deve ser válido")
        @NotBlank(message = "O email não pode estar vazio")
        String email,
        @Pattern(regexp = "\\d{9}", message = "O RG deve conter 9 dígitos numéricos")
        String rg,
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
        String cpf,
        @NotBlank(message = "A senha não pode estar vazia")
        String senha
) {
}
