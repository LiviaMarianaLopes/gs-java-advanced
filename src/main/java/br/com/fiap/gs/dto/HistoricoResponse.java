package br.com.fiap.gs.dto;

import br.com.fiap.gs.model.Cadastro;
import br.com.fiap.gs.model.Veiculo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record HistoricoResponse(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        Date data,
        String localPartida,
        String localDestino,
        int tempoViagem,
        String percurso,
        Cadastro cadastro,
        Veiculo veiculo
) {
}
