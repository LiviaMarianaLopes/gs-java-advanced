package br.com.fiap.gs.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
public enum TipoVeiculo {
    B('B'),
    P('P');
    private char tipo;

    @Override
    public String toString() {
        return "TipoVeiculo{" +
                "tipo=" + tipo +
                '}';
    }
}
