package br.com.fiap.gs.dto;

public record VeiculoResponse(
        Long id,
        String numeroDeSerie,
        String latitude,
        String longitude,
        String tipoVeiculo
) {
}
