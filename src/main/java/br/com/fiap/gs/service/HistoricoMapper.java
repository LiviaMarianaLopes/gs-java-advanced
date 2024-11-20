package br.com.fiap.gs.service;

import br.com.fiap.gs.dto.HistoricoRequest;
import br.com.fiap.gs.dto.HistoricoResponse;
import br.com.fiap.gs.model.Cadastro;
import br.com.fiap.gs.model.Historico;
import br.com.fiap.gs.model.Veiculo;
import br.com.fiap.gs.repository.CadastroRepository;
import br.com.fiap.gs.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricoMapper {
    @Autowired
    CadastroRepository cadastroRepository;
    @Autowired
    VeiculoRepository veiculoRepository;

    public Historico requestToHistorico(HistoricoRequest historicoRequest) {
        Cadastro cadastro = cadastroRepository.findById(historicoRequest.idCadastro())
                .orElseThrow(() -> new RuntimeException("Cadastro não encontrado"));

        Veiculo veiculo = veiculoRepository.findById(historicoRequest.idVeiculo())
                .orElseThrow(() -> new RuntimeException("Veiculo não encontrado"));
        Historico historico = new Historico();
        historico.setData(historicoRequest.data());
        historico.setLocalPartida(historicoRequest.localPartida());
        historico.setLocalDestino(historicoRequest.localDestino());
        historico.setTempoViagem(historicoRequest.tempoViagem());
        historico.setPercurso(historicoRequest.percurso());
        historico.setCadastro(cadastro);
        historico.setVeiculo(veiculo);
        return historico;
    }

    public HistoricoResponse historicoToResponse(Historico historico) {
        return new HistoricoResponse(
                historico.getId(),
                historico.getData(),
                historico.getLocalPartida(),
                historico.getLocalDestino(),
                historico.getTempoViagem(),
                historico.getPercurso(),
                historico.getCadastro(),
                historico.getVeiculo()
        );
    }

}
