package br.com.fiap.gs.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GS_HISTORICO")
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_uso")
    private Date data;
    @Column(name = "local_partida")
    private String localPartida;
    @Column(name = "local_destino")
    private String localDestino;
    @Column(name = "tempoViagem")
    private int tempoViagem;
    @Column(name = "percurso")
    private String percurso;
    @ManyToOne
    @JoinColumn(name = "id_cadastro", referencedColumnName = "id")
    private Cadastro cadastro;
    @OneToMany
    @JoinColumn(name = "id_veiculo", referencedColumnName = "id")
    private Veiculo veiculo;
}

