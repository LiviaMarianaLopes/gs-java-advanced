package br.com.fiap.gs.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.sql.Date;

public record HistoricoRequest(
        @NotNull(message = "A data da é obrigatória")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        Date data,
        @NotBlank(message = "Local da partida é obrigatório")
        String localPartida,
        @NotBlank(message = "Local do destino é obrigatório")
        String localDestino,
        @NotNull(message = "O tempo da viagem é obrigatório ")
        @Positive(message = "Tempo inválido")
        int tempoViagem,
        @NotBlank(message = "A kilometragem do percurso é obrigatória")
        String percurso,
        @NotNull(message = "O id do cadastro é obrigatório")
        @Positive(message = "Id do cadastro inválido")
        Long idCadastro,
        @NotNull(message = "O id do veiculo é obrigatório")
        @Positive(message = "Id do veiculo inválido")
        Long idVeiculo
) {
}
