package br.com.fiap.gs.service;

import br.com.fiap.gs.dto.VeiculoRequest;
import br.com.fiap.gs.dto.VeiculoResponse;
import br.com.fiap.gs.model.Veiculo;
import org.springframework.stereotype.Service;

@Service
public class VeiculoMapper {

    public Veiculo requestToVeiculo(VeiculoRequest veiculoRequest) {
        Veiculo veiculo = new Veiculo();
        veiculo.setNumeroSerie(veiculoRequest.numeroDeSerie());
        veiculo.setLatitude(veiculoRequest.latitude());
        veiculo.setLongitude(veiculoRequest.longitude());
        veiculo.setTipoVeiculo(veiculoRequest.tipoVeiculo());
        return veiculo;
    }

    public VeiculoResponse veiculoToResponse(Veiculo veiculo) {
        return new VeiculoResponse(
                veiculo.getId(),
                veiculo.getNumeroSerie(),
                veiculo.getLatitude(),
                veiculo.getLongitude(),
                veiculo.getTipoVeiculo().toString()
        );
    }


}
