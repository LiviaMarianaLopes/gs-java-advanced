package br.com.fiap.gs.dto;

import br.com.fiap.gs.model.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoRequest(
        @NotBlank(message = "O número de série é obrigatório")
        String numeroDeSerie,
        @NotBlank(message = "A latitude é obrigatória")
        String latitude,
        @NotBlank(message = "A longitude é obrigatória")
        String longitude,
        @NotNull(message = "O tipo do veículo é obrigatório")
        TipoVeiculo tipoVeiculo
) {
}
