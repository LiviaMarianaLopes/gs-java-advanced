package br.com.fiap.gs.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum TipoVeiculo {
    B,
    P;
    private char tipoVeiculo;
}
